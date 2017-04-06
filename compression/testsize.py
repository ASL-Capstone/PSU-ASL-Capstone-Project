#!/usr/bin/env python3
import argparse
import multiprocessing
import queue
import sys
import os
import shutil
import tempfile
from skvideo import io as vidio

import numpy as np

import passes

class ChainWorker(multiprocessing.Process):
    """Utility class for multi-process chain workers
    
    Accepts an input and output queue. The input queue should supply tuples of
    the form (frame_num, shape, array). The array should be stored in shared
    memory. Its contents will be reinterpreted as a 2D numpy array of floats
    and run through the filter chain. Once the chain has finished, a tuple
    (frame_num, array) will be sent to the output queue.
    """

    def __init__(self, in_q, out_q, chain, *args, **kwargs):
        multiprocessing.Process.__init__(self, *args, **kwargs)

        self.chain = chain
        self.in_q = in_q
        self.out_q = out_q
        self.term_evt = multiprocessing.Event()

    def run(self):
        chain = [p.MAIN_CLASS(ns, meta) for p, ns, meta in self.chain]

        while not self.term_evt.is_set():
            # Read a frame
            try:
                frame_num, shape, frame = self.in_q.get(timeout=0.5)
            except queue.Empty:
                continue
            
            # Actually run the processing chain
            for c in chain:
                frame = c.process(frame)

            # Write the result back to the output queue
            self.out_q.put((frame_num, frame))

    def stop(self):
        self.term_evt.set()
        self.join()

class ChainPool:
    """Multiprocess worker pool to run video processing chains"""

    def __init__(self, chain, frames, num_workers=os.cpu_count()):
        self.iq = multiprocessing.Queue()
        self.oq = multiprocessing.Queue()
        
        subchain = [(p, ns, p.prepare(ns, np.asarray(frames))) for p, ns in chain]

        self.workers = []
        for i in range(num_workers):
            self.workers.append(ChainWorker(self.iq, self.oq, subchain))
        for i in self.workers:
            i.start()

    def map(self, frames):
        """Take an iterable of Numpy frames, run them concurrently through the
        video processing chain, and return the processed frames in order."""
        itr = iter(frames)

        pushed = 0
        next_return = 0
        returned = 0

        res_accum = {}

        while True:
            if (returned - pushed) < 2*len(self.workers):
                try:
                    f = next(itr)
                except StopIteration:
                    break
                self.iq.put((pushed, f.shape, f))
                pushed += 1
            try:
                fn,rf = self.oq.get(timeout=0.5)
                res_accum[fn] = rf
                returned += 1
            except queue.Empty:
                continue

            if next_return in res_accum:
                yield res_accum[next_return]
                del res_accum[next_return]
                next_return += 1

        while returned < pushed:
            fn,rf = self.oq.get()
            res_accum[fn] = rf
            returned += 1
            if next_return in res_accum:
                yield res_accum[next_return]
                del res_accum[next_return]
                next_return += 1

        while next_return < pushed:
            yield res_accum[next_return]
            del res_accum[next_return]
            next_return += 1

    def stop(self):
        for w in self.workers:
            w.stop()

# Parse common arguments
parser = argparse.ArgumentParser("testsize",
        description="Test specific video compression pipeline")
parser.add_argument("-s", "--save", action="store_true",
        help="Save the encoding result for visual inspection")
parser.add_argument("-j", "--threads", type=int, default=1,
        help="The number of workers to use for analysis")
parser.add_argument("-p", "--perf", action="store_true",
        help="Collect and print performance metrics")
parser.add_argument("-n", "--allow-nop", action="store_true",
        help="Allow execution of zero-length pass chains")
parser.add_argument("input", type=str,
        help="The video file to use as input")
base_args, remain = parser.parse_known_args()

if not os.path.exists(base_args.input):
    print("Error: Cannot open file '{}'".format(base_args.input))
    sys.exit(1)

# Parse pass chain
pass_chain = []
while len(remain) > 0:
    if not hasattr(passes, remain[0]):
        print("Error: No pass named '{}'".format(remain[0]))
        print("Available passes:")
        for pname in dir(passes):
            if pname.startswith("_"):
                continue
            print("\t{}".format(pname))
        exit(1)
    p = getattr(passes, remain[0])
    ns, remain = p.ARGS.parse_known_args(remain[1:])
    pass_chain.append((p, ns))

if len(pass_chain) == 0 and not base_args.allow_nop:
    print("No passes specified. Terminating.")
    exit(0)

# Start processing the input file
frames = list(vidio.vreader(base_args.input))
print(frames[0].shape)
print("Setting up pipeline...")
print("Using {} workers".format(base_args.threads))
pool = ChainPool(pass_chain, frames, base_args.threads)

print("Processing video...")
scanned = 0
out_frames = []
termsize = shutil.get_terminal_size()
for f in pool.map(frames):
    scanned += 1
    out_frames.append(f)

    # Update UI
    frac = scanned / len(frames)
    sys.stdout.write('[{:6.2f}%]'.format(100 * frac))
    sys.stdout.write('='*int(frac * (termsize.columns-9) - 1) + '>\r')
    sys.stdout.flush()

print("\nDone. Waiting for worker processes...")
pool.stop()
print("Filter chain complete")

# Run size analysis on the results. This re-encodes the input to eliminate the
# input file's encoding parameters as a source of error.

def get_size(vid, filename=None):
    if filename:
            wr = vidio.FFmpegWriter(filename, outputdict={
                '-vcodec': 'libx264',
                '-s': '{}x{}'.format(vid[0].shape[1], vid[0].shape[0])})
                #'-crf': '10'})
            for frame in vid:
                wr.writeFrame(frame)
            wr.close()
            return os.stat(filename).st_size
    else:
        with tempfile.NamedTemporaryFile(suffix='.mkv') as f:
            wr = vidio.FFmpegWriter(f.name, outputdict={
                '-vcodec': 'libx264',
                '-crf': '10'})
            for frame in vid:
                wr.writeFrame(frame)
            wr.close()
            return os.stat(f.name).st_size

print("Analyzing original video...")
orig_size = get_size(list(vidio.vreader(base_args.input)), "/tmp/temp.mkv")
print("Analyzing filtered video...")
result_size = get_size(out_frames,
        "/tmp/out_{}.mkv".format(os.path.basename(base_args.input))
            if base_args.save else None)
print("----------------------")
print("Initial size:   {} bytes".format(orig_size))
print("Final size:     {} bytes".format(result_size))
print("Size reduction: {:.02f}%".format(100*(orig_size-result_size)/orig_size))
