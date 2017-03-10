import argparse
import numpy as np
from scipy import ndimage

class DownscalePass:
    """A filter pass which scales down the input frames"""

    def __init__(self, args, meta):
        self.args = args
        self.meta = meta

    def process(self, frame):
        """Process an input frame in place"""
        # compute zoom
        zoom = 0
        if self.args.target != None:
            max_axis = max(frame.shape[0], frame.shape[1])
            zoom = self.args.target / max_axis
        elif self.args.factor != None:
            zoom = self.args.factor
        else:
            zoom = 0.5

        r,g,b = frame[:,:,0],frame[:,:,1],frame[:,:,2]

        return np.dstack((
            ndimage.zoom(r, zoom),
            ndimage.zoom(g, zoom),
            ndimage.zoom(b, zoom)))

def prepare(args, frames):
    pass

ARGS = argparse.ArgumentParser("downscale",
        description="Downscaling pass which reduces the frames' size")
ARGS.add_argument('-t', '--target', type=int, help="Target maximum edge length")
ARGS.add_argument('-f', '--factor', type=float, help="Fixed scale factor")
MAIN_CLASS = DownscalePass
