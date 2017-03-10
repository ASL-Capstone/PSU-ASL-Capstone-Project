import argparse
import numpy as np
from scipy import ndimage

class FourierPass:
    """A filter pass which reduces image complexity by removing high-frequency
    visual components."""

    def __init__(self, args, meta):
        self.args = args
        self.meta = meta

    def process(self, frame):
        """Process an input frame in place"""
        # the (0,1) tells Numpy to compute the FFT over the spatial axes and not
        # the color axis
        red, green, blue = frame[:,:,0],frame[:,:,1],frame[:,:,2]

        r_freq_domain = np.fft.fft2(red)
        r_clipped = ndimage.fourier_uniform(r_freq_domain, self.args.freq)
        r_final = np.fft.ifft2(r_clipped)

        g_freq_domain = np.fft.fft2(green)
        g_clipped = ndimage.fourier_uniform(g_freq_domain, self.args.freq)
        g_final = np.fft.ifft2(g_clipped)

        b_freq_domain = np.fft.fft2(blue)
        b_clipped = ndimage.fourier_uniform(b_freq_domain, self.args.freq)
        b_final = np.fft.ifft2(b_clipped)

        frame[:,:,0] = r_final
        frame[:,:,1] = g_final
        frame[:,:,2] = b_final
        return frame

def prepare(args, frames):
    pass

ARGS = argparse.ArgumentParser("fourier",
        description="Frequency-domain filtering pass")
ARGS.add_argument('-f', '--freq', type=int, default=10,
        help='Frequency cutoff adjustment')
ARGS.add_argument('-s', '--soft', action='store_true',
        help='Use a soft falloff rather than a hard cutoff')
MAIN_CLASS = FourierPass

