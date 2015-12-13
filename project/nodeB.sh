#!/bin/sh

java -cp $1 gr.kzps.id2212.project.agentserver.ServerExecEnv -i server1 \
-s NodeB/ -bp 9191 -ap 6161 -bsn localhost -bsp 9090
