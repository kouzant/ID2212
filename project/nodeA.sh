#!/bin/sh

java -cp $1 gr.kzps.id2212.project.agentserver.ServerExecEnv -i server0 -s NodeA/ \
-ap 6060 -bp 9090
