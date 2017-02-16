import argparse

class IdentityPass:
    """A filter pass which does nothing, just returning the input frame.
    
    This pass can act as a template for other pass types."""

    def __init__(self, args):
        self.args = args

    def process(self, frame):
        """Process an input frame in place"""
        pass

ARGS = argparse.ArgumentParser("identity",
        description="Identity pass that doesn't change the input")
MAIN_CLASS = IdentityPass
