#!/bin/bash

if [ "$#" -ge 1 ]; then
    while getopts "p:" opt; do
	case $opt in
	    p)
		java -cp target/Hangman-0.0.1-SNAPSHOT.jar gr.kzps.id2212.hangman.server.TcpServer --port $OPTARG
		;;
	    \?)
		echo "Invalid option: -$OPTARG" >&2
		;;
	    :)
		echo "Option -$OPTARG requires an argument." >&2
		exit 1
		;;
	esac
    done
else
    java -cp target/Hangman-0.0.1-SNAPSHOT.jar gr.kzps.id2212.hangman.server.TcpServer
fi
