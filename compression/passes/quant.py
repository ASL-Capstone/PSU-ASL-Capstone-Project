import argparse
import scipy.cluster as cluster

class QuantizePass:
    """A filter pass which quantizes the input."""

    def __init__(self, args, centroids):
        self.args = args
        self.centroids = centroids

    def process(self, frame):
        """Process an input frame in place"""
        width,height,channels = frame.shape
        flat = frame.reshape((width*height,channels)).astype('float')
        labels, dists = cluster.vq.vq(flat, self.centroids)
        labels = labels.reshape((width,height))
        frame[:] = self.centroids[labels]

def prepare(args, frames):
    """Synchronous preparation step, to compute mean colors over the entire
    video

    Returns cluster centers."""
    count,width,height,channels = frames.shape
    flat = frames.reshape((count*width*height,channels)).astype('float')
    centroids, labels = cluster.vq.kmeans2(flat, args.clusters,
            iter=15, minit='points')
    return centroids

ARGS = argparse.ArgumentParser("quantize",
        description="Color quantization pass, to reduce the variation in the image")
ARGS.add_argument('-k', '--clusters', type=int, help='Number of colors to use',
        default=8)
MAIN_CLASS = QuantizePass

