#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

#define DEBUG 1

/*util*/
void genArr(int *arr, int len, int max);//生成随机数 len:长度 max:最大数
void swap(int *arr, int i, int j);//交换
void printArr(int *arr, int len);//打印数组
int comp(const void*a,const void*b);//用来做比较的函数。
int assertEqual(int *arr1, int *arr2, int len);//判断两个数组是否一样,0-false,1-true

typedef void (*msort)(int *arr,int len);//定义函数指针
msort getFuncPointer(char* fname);

/*排序算法*/
void selection(int *arr,int len);//选
void bubble(int *arr, int len);//泡
void insertion(int *arr, int len);//插
void quick(int *arr, int len);//快
void merge(int *arr, int len);//归
void heap(int *arr, int len);//堆
void shell(int *arr, int len);//希
void bucket(int *arr, int len);//桶
void counting(int *arr, int len);//计
void radix(int *arr, int len);//基

/*test*/
void testGenArr();
void testSwap();


void testSelection();
void testBubble();
void testInsertion();
void testQuick();
void testMerge();
void testHeap();
void testShell();
void testBucket();
void testCounting();
void testRadix();
void testSort(char *s,void (*msort)(int *,int));

msort getFuncPointer(char* fname){
	if(strncmp(fname,"selection",9)==0)
    {
         return &selection;
    }else if(strncmp(fname,"bubble",6)==0){
         return &bubble;
    }else if(strncmp(fname,"insertion",9)==0){
         return &insertion;
    }else if(strncmp(fname,"shell",5)==0){
         return &shell;
    }
    return NULL;
}

int main(int argc, char* argv[]){
	
	printf("%d\n",argc);
	msort m ;
	if(argc < 2){
		m = selection;
	}else{
		m = getFuncPointer(argv[1]);	
	}

	if(m == NULL){
		printf("error param!!!\n");
		return -1;
	}
	//msort = bubble;
	//msort = insertion;
	
	char *sort_type = "bubble sort";
	testSort(sort_type, m);

	printf("hello world.");
	return 0;
}

/* sort start ... */
/* 优化：1.一次找出最大最小放到开始和结束 2.一次比较三个数 */
/*思考：1.没有优化的空间 2.验证它为什么不稳定*/
void selection(int *arr,int len){
	if(DEBUG){
		printf("selection sort start...\n");
	}
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
	if(DEBUG){
		printf("selection sort end...\n");
	}
}

void bubble(int *arr, int len){
	if(DEBUG){
		printf("bubble sort start...\n");
	}
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
	if(DEBUG){
		printf("bubble sort end...\n");
	}
}

void insertion(int *arr, int len){
	if(DEBUG){
		printf("insertion sort start...\n");
	}
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
	if(DEBUG){
		printf("insertion sort end...\n");
	}
}

void quick(int *arr, int len){

}

void merge(int *arr, int len){

}

void heap(int *arr, int len){

}

void shell(int *arr, int len){
	int gap = 4,i,j,k;
	for(k = gap; k>0;k--){

		for(int j = gap;j<len;j+=gap){
			int key = arr[j];
			i = j - gap;
			while(i >=0 && arr[i] > key){
				arr[i+gap] = arr[i];
				i -= gap;
			}
			arr[i+gap] = key;
		}
		if(DEBUG){
			printf("round %d:\n",gap-k+1);
			printArr(arr,len);
		}
	}
}

void bucket(int *arr, int len){

}

void counting(int *arr, int len){

}

void radix(int *arr, int len){

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

//*
void testSort(char *s,void (*msort)(int *,int)){
	int arr[15];
	int arr1[15];
	int len = 15;
	int max = 100;
	
	genArr(arr,len,max);
	memcpy(arr1,arr,sizeof(int)*len);
	
	printf("%s:\n",s);
	printf("\nsource:\n");
	printArr(arr,len);	
	
	qsort(arr1,len,sizeof(int),comp);
	printf("\nsys:\n");
	printArr(arr1,len);

	printf("\nimpl:\n");
	msort(arr,len);//回调函数
	printArr(arr,len);

	//1.与确定正确的算法对比
	//2.为防止偶然，可使用足够多的随机样本，都成功则测试成功
	int ret = assertEqual(arr,arr1,len);
	printf("\nret:%d\n",ret);
	
}
//*/

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
