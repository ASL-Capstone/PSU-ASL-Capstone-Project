package com.psu.capstonew17.backend;

import java.io.IOException;

/**
 * Created by codyborst on 5/13/17.
 */

public interface EncodeableObject {
    /*
    convert object to an array of bytes
     */
    byte[] encodeToByteArray() throws IOException;
}
