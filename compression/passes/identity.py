import argparse

class IdentityPass:
    """A filter pass which does nothing, just returning the input frame.
    
    This pass can act as a template for other pass types."""

    def __init__(self, args, meta):
        self.args = args
        self.meta = meta

    def process(self, frame):
        """Process an input frame in place"""
        pass

def prepare(args, frames):
    """Synchronous preparation step, accepting the entire video. May not
    mutate the video frames.

    Should return an arbitrary object that will be passed to the pass class's
    constructor."""
    pass

ARGS = argparse.ArgumentParser("identity",
        description="Identity pass that doesn't change the input")
MAIN_CLASS = IdentityPass
