#!/bin/bash

export PATH=$PATH:$HOME/bin

cd $HOME/installs/films
lein run -m films.core

