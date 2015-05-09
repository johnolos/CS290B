#!/bin/bash
gnome-terminal --tab="Compiling" -x bash -c "ant all"
sleep 4
gnome-terminal --title="Space" -x bash -c "ant runSpace"
sleep 2

tab="--tab"
title="-t "
cmd="bash -c 'ant runComputer';bash"
params=""
n=4
for i in $(seq 1 $n); do
    params+=($tab $title "Computer $i" -e "$cmd")
done
gnome-terminal "${params[@]}"
gnome-terminal --title="Task"
exit 0
