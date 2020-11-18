#!/usr/bin/python

# Inputs: figure save path, also used to load in attribute/timepoint inforamtion gain from text file
#         seed, used in save path
#
# Author: Matthew Middlehurst

import sys
import numpy as np
from utilities import *
from matplotlib import pyplot as plt
from matplotlib import cm

plt.rcParams["font.family"] = "Calibri"

f = open(sys.argv[1] + 'interp' + sys.argv[2] + '.txt', 'r')

series = array_string_to_list_float(f.readline())
slices = deep_array_string_to_list_int(f.readline())
pred_class = int(f.readline())
num_classes = int (f.readline())

coeffs = []
for i in range(0, num_classes):
	f.readline()
	coeffs.append(array_string_to_list_float(f.readline()))

fig, axs = plt.subplots()
axs.plot(series)

for i in range(0, len(slices)):
    if coeffs[pred_class][i] > 0:
        c = 'r'
    elif coeffs[pred_class][i] < 0:
        c = 'b'
    else:
        continue

    plt.axvspan(slices[i][0], slices[i][1], facecolor=c, alpha=abs(coeffs[pred_class][i]*5))

plt.savefig(sys.argv[1] + 'interp' + sys.argv[2], bbox_inches='tight')