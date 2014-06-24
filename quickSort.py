import sys
import array

#input = [3,2,7,1,9,6]
input = []
count = 0

with open("QuickSort.txt") as f:
	for line in f:
		input.append(int(line.strip('\n\r')))


def quickSortPartition(l, r, pivotType):
	if(pivotType == 1):
		swap(l, r)
	elif(pivotType == 2):
		getMedian(l,r)

	pivot = input[l]
	i = l + 1

	for j in range(l+1, r+1):
		if(input[j] < pivot):
			swap(i,j)
			i += 1

	swap(l, i-1)
	return i-1

def quickSort(l, r, pivotType):
	global count
	if(l < r):
		count += (r - l)
		idx = quickSortPartition(l, r, pivotType)
		quickSort(l, idx -1, pivotType)
		quickSort(idx + 1, r, pivotType)

def swap(i, j):
	temp = input[i]
	input[i] = input[j]
	input[j] = temp


def getMedian(i, j):
	mid = (i + j)//2
	small = 0
	large = 0

	if(input[i] > input[mid]):
		small = mid
		large = i
	else:
		small = i
		large = mid

	if(input[j] > input[large]):
		mid = large
	elif(input[j] < input[small]):
		mid = small
	else:
		mid = j

	swap(i, mid)

quickSort(0, len(input)-1, 2)

#print(getMedian(4,5))

#print(len(input))
print(count)
#print(input)
