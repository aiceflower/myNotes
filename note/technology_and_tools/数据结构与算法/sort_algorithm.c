#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

#define DEBUG 0

/*util*/
void genArr(int *arr, int len, int max);//生成随机数 len:长度 max:最大数
void swap(int *arr, int i, int j);//交换
void printArr(int *arr, int len);//打印数组
int comp(const void*a,const void*b);//用来做比较的函数。
int assertEqual(int *arr1, int *arr2, int len);//判断两个数组是否一样,0-false,1-true

/*排序算法*/
void selection(int *arr,int len);//选
void bubble(int *arr, int len);//泡
void insertion(int *arr, int len);//插

/*test*/
void testGenArr();
void testSwap();
void testSelection();
void testBubble();
void testInsertion();

int main(){
	//testGenArr();
	//testSwap();
	testSelection();
	//testBubble();
	//testInsertion();
	printf("hello world.");
	return 0;
}

/* sort start ... */
/* 优化：1.一次找出最大最小放到开始和结束 2.一次比较三个数 */
void selection(int *arr,int len){
	int i,j;
	for(i = 0; i < len-1; i++){
		int min = i;
		for(j = i+1; j < len; j++){
			min = arr[min] > arr[j] ? j : min;
		}
		swap(arr,i,min);
		if(DEBUG){
			printf("round %d:\n",i+1);
			printArr(arr,len);
		}
	}
}

void bubble(int *arr, int len){
	int i,j;
	for(i = len; i > 0; i--){
		for(j = 0; j < i - 1; j++){
			if(arr[j] > arr[j+1]){
				swap(arr,j,j+1);
			}
		}
		if(DEBUG){
			printf("round %d:\n",len-i+1);
			printArr(arr,len);
		}
	}
}

void insertion(int *arr, int len){
	int i,j;
	for(j = 1; j < len; j++){
		int key = arr[j];
		i = j -1;
		while(i >= 0 && arr[i] > key){//从后往前插入，移动到比key小的数为止
			arr[i+1] = arr[i];
			i--;
		}
		arr[i+1] = key;//key的正确位置
		if(DEBUG){
			printf("round %d:\n",j-1);
			printArr(arr,len);
		}
	}
}
/* sort end ... */

/* test start ... */
void testGenArr(){
	int arr[15];
	genArr(arr,15,100);
	printArr(arr,15);
}

void testSwap(){
	int arr[2] = {1,2};
	printf("a=%d,b=%d\n",arr[0],arr[1]);
	swap(arr,0,1);
	printf("a=%d,b=%d\n",arr[0],arr[1]);
}

void testSelection(){
	int arr[15];
	int arr1[15];
	int len = 15;
	int max = 100;
	
	genArr(arr,len,max);
	memcpy(arr1,arr,sizeof(int)*len);
	
	printf("source:\n");
	printArr(arr,len);	
	
	qsort(arr1,len,sizeof(int),comp);
	printf("sys:\n");
	printArr(arr1,len);

	printf("impl:\n");
	selection(arr,len);
	printArr(arr,len);

	int ret = assertEqual(arr,arr1,len);
	printf("ret:%d\n",ret);
	
}

void testBubble(){
	int arr[15];
	int arr1[15];
	int len = 15;
	int max = 100;
	
	genArr(arr,len,max);
	memcpy(arr1,arr,sizeof(int)*len);
	
	printf("source:\n");
	printArr(arr,len);	
	
	qsort(arr1,len,sizeof(int),comp);
	printf("sys:\n");
	printArr(arr1,len);

	printf("impl:\n");
	bubble(arr,len);
	printArr(arr,len);

	int ret = assertEqual(arr,arr1,len);
	printf("ret:%d\n",ret);
}

void testInsertion(){
	int arr[15];
	int arr1[15];
	int len = 15;
	int max = 100;
	
	genArr(arr,len,max);
	memcpy(arr1,arr,sizeof(int)*len);
	
	printf("source:\n");
	printArr(arr,len);	
	
	qsort(arr1,len,sizeof(int),comp);
	printf("sys:\n");
	printArr(arr1,len);

	printf("impl:\n");
	insertion(arr,len);
	printArr(arr,len);

	int ret = assertEqual(arr,arr1,len);
	printf("ret:%d\n",ret);
}
/* test end ... */

/* util start ... */
void printArr(int *arr, int len){//打印数组
	int i;
	for(i = 0;i<len;i++){
		printf("%-6d",arr[i]);
		if((i+1)%10==0)
			printf("\n");
	}
	printf("\n");
}

void genArr(int *arr, int len, int max){//生成指定范围内指定长度的随机数
	srand(time(0));//随机种子
	int i;
	for(i=0;i<len;i++) arr[i] = rand() % max;
}

void swap(int *arr, int i, int j){//交换数组中两个元素位置
	int tmp = arr[i];
	arr[i] = arr[j];
	arr[j] = tmp;
}

int comp(const void*a,const void*b)//用来做比较的函数。
{
    return *(int*)a-*(int*)b;
}

int assertEqual(int *arr1, int *arr2, int len){//判断两个数组是否相等
	int i;
	for(i=0;i<len;i++){
		if(arr1[i] != arr2[i]){
			return 0;
		}
	}
	return 1;
}
/* util end ... */
