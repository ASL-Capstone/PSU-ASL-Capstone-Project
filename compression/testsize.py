#!/usr/bin/env python3
import argparse
import multiprocessing
import queue
import sys
import os

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
        chain = [p(ns) for p, ns in self.chain]

        while not self.term_evt.is_set():
            # Read a frame
            try:
                frame_num, shape, arr = self.in_q.get(timeout=2)
            except queue.Empty:
                continue
            with arr.get_lock():
                frame = np.frombuffer(arr.get_obj())
                frame.reshape(shape)
                
                # Actually run the processing chain
                for c in chain:
                    c.process(frame)

            # Write the result back to the output queue
            self.out_q.put((frame_num, arr))

    def stop(self):
        self.term_evt.set()
        self.join()

class ChainPool:
    """Multiprocess worker pool to run video processing chains"""

    def __init__(self, workers=os.cpu_count()):

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
parser.add_argument("input", type=argparse.FileType('rb'),
        help="The video file to use as input")
base_args, remain = parser.parse_known_args()

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

# Start decoding the input file
