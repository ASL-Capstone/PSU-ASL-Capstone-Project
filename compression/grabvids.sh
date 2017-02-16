#!/usr/bin/zsh
# Download test videos from Youtube, strip audio tracks, and transcode to a
# form that's easy to read into Python. This script requires youtube-dl and
# ffmpeg to be installed on the host system.
#
# Videos aren't directly included in repository for copyright reasons

mkdir -p videos

sed -ne 's|^\(.*\)$|http://youtube.com/watch?v=\1|p' |\
    xargs youtube-dl --no-cache-dir -o 'videos/vid_%(id)s.%(ext)s'

# count CPU cores for multithreaded transcoding
CORES=$(lscpu | sed -rne 's/^CPU(s): *\([0-9]+\)$/\1/p')

echo "Using ${CORES} concurrent transcoders"
ls videos | xargs -n 1 -P ${CORES} -i ffmpeg -i {} \
    -an -c:v libx264 -preset fast -crf 18 {}.mkv
