#pragma version(1)
#pragma rs java_package_name(com.psu.capstonew17.backend.video)

uint32_t k; // number of means
rs_allocation centers;

uint32_t RS_KERNEL assign_clusters(uchar3 rgb) {
    uint32_t best = 0;
    float best_d = 9999999;
    for(int i = 0;i < k;i++) {
        uchar3 mean = rsGetElementAt_uchar3(centers, i);
        float d = fast_distance(centers[0], mean);
        if(d < best_d) {
            best = i;
            best_d = d;
        }
    }

    return best;
}

uint8_t RS_KERNEL
