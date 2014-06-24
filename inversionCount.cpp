#include <iostream>
#include <vector>
#include <fstream>
#include <cstdlib>

using namespace std;
int * countInversions(int*, int, unsigned long *);
unsigned long countInversionsInTheStupidWay(int*, int);

int main (int argc, char *argv[]){

	if(argc == 1) return 0;
	int size = 100000;

	int arr[size];


	fstream fp;
	fp.open(argv[1], fstream::in);

	if (!fp.is_open()){
			cout << "ERROR opening " << *argv[1] << endl;
	}

	char buffer[20];
	int i = 0;

	while(!fp.eof()){
		fp.getline(buffer,20);
		arr[i] = atoi(buffer);
		i++;
		if (i == size) break;
	}

	unsigned long numOfInversions = 0;
	int * a;
	unsigned long b = 0;

	a = new int [size];

	//b = countInversionsInTheStupidWay(arr, size);
	cout << "Counting inversions now ...";
	a = countInversions(arr, size, &numOfInversions);


	cout << "Number of Inversions 					:  " << numOfInversions << endl;
	//cout << "Number of Inversions in the stupid way :  " << b << endl;

	fp.close();

	return 0;
}

int * countInversions(int *arr, int size, unsigned long *count){

	if (size == 1) return arr;
	int size_l, size_h;

	if (size%2 == 0 ){
		size_l = size/2;
		size_h = size/2;
	}
	else {
		size_l = size/2;
		size_h = size/2 + 1;
	}

	int arr_l[size_l];
	int arr_h[size_h];

	int *s_arr_l = new int[size_l];
	int *s_arr_h = new int[size_h];

	if(size != size_l + size_h) {
		cout<< "Fatal Error!" << endl;
	}

	for (int i = 0; i < size_l ; i++){
		arr_l[i] = arr[i];
	}
	for (int i = size_l; i < size ; i++){
		arr_h[i - size_l] = arr[i];
	}

	s_arr_l = countInversions(arr_l, size_l, count);
	s_arr_h = countInversions(arr_h, size_h, count);

	int l_idx = 0;
	int h_idx = 0;

	for (int i = 0; i < size ; i++){

		if(l_idx == size_l){
			arr[i] = s_arr_h[h_idx];
			h_idx++;
			continue;
		}
		if(h_idx == size_h){
			arr[i] = s_arr_l[l_idx];
			l_idx++;
			continue;
		}
		if (s_arr_l[l_idx] <= s_arr_h[h_idx]){
			arr[i] = s_arr_l[l_idx];
			l_idx++;
			continue;
		}
		else {
			arr[i] = s_arr_h[h_idx];
			*count = *count + size_l - l_idx;
			h_idx++;
			continue;
		}
	}
return arr;
}

unsigned long countInversionsInTheStupidWay(int *arr, int size){
	unsigned long count = 0;

	for(int i = 0 ; i < size-1; i++ )
		for (int j = i; j < size; j++)
			if(arr[i] > arr[j]) count++;

	return count;
}

