//  data_structure
//
//  Created by alonglamp on 2020/11/10.
//
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

#define DEBUGER 0
char *sort_type;
/*  */

/*util*/
void genArr(int *arr, int len, int max);//生成随机数 len:长度 max:最大数
void swap(int *arr, int i, int j);//交换
void printArr(int *arr, int len);//打印数组
int comp(const void*a,const void*b);//用来做比较的函数。
int assertEqual(int *arr1, int *arr2, int len);//判断两个数组是否一样,0-false,1-true
void sub_merge(int *arr,int lp,int rp,int rb);//left_pointer,rignt_pointer,right_bound
typedef void (*msort)(int *arr,int len);//定义函数指针
msort getFuncPointer(char* fname);

/*排序算法*/
void selection(int *arr,int len);//选
void bubble(int *arr, int len);//泡
void insertion(int *arr, int len);//插
void quick(int *arr, int len);//快
void merge(int *arr, int left,int right);//归
void heap(int *arr, int len);//堆
void shell(int *arr, int len);//希
void bucket(int *arr, int len);//桶
void counting(int *arr, int len,int max);//计
void counting1(int *arr, int len,int max);//计
void radix(int *arr, int len);//基

/*test*/
void testGenArr(void);
void testSwap(void);

void testSort(char *s,void (*msort)(int *,int));
void testMergeSort(void);
void testCountingSort(void);
void testCounting1Sort(void);

msort getFuncPointer(char* fname){
	if(strncmp(fname,"selection",9)==0)
    {
         sort_type = "selection sort";
         return &selection;
    }else if(strncmp(fname,"bubble",6)==0){
         sort_type = "bubble sort";
         return &bubble;
    }else if(strncmp(fname,"insertion",9)==0){
        sort_type = "insertion sort";
         return &insertion;
    }else if(strncmp(fname,"shell",5)==0){
        sort_type = "shell sort";
         return &shell;
    }
    return NULL;
}

int main(int argc, char* argv[]){
	//*
	printf("%d\n",argc);
	msort m ;
	if(argc < 2){
		m = selection;
	}else{
		m = getFuncPointer(argv[1]);	
	}
	if(m == NULL){
        printf("func:%s\n",argv[1]);
        if(strncmp(argv[1],"merge",5)){
            testMergeSort();
            return 0;
        }
        if(strncmp(argv[1],"counting",8)){
            testCountingSort();
            return 0;
        }
        if(strncmp(argv[1],"counting1",9)){
            testCounting1Sort();
            return 0;
        }
		printf("error param!!!\n");
		return -1;
	}
	//msort = bubble;
	//msort = insertion;
	
	testSort(sort_type, m);
    // */
    printf("hello world.\n");
	return 0;
}

/* sort start ... */
/* 可再优化：1.一次找出最大最小放到开始和结束 2.一次比较三个数 */
/* 思考：1.没有优化的空间 2.验证它为什么不稳定 */
void selection(int *arr,int len){
	if(DEBUGER){
		printf("selection sort start...\n");
	}
	int i,j;
	for(i = 0; i < len-1; i++){
		int min = i;
		for(j = i+1; j < len; j++){
			min = arr[min] > arr[j] ? j : min;
		}
		swap(arr,i,min);
		if(DEBUGER){
			printf("round %d:\n",i+1);
			printArr(arr,len);
		}
	}
	if(DEBUGER){
		printf("selection sort end...\n");
	}
}

void bubble(int *arr, int len){
	if(DEBUGER){
		printf("bubble sort start...\n");
	}
	int i,j;
	for(i = len; i > 0; i--){
		for(j = 0; j < i - 1; j++){
			if(arr[j] > arr[j+1]){
				swap(arr,j,j+1);
			}
		}
		if(DEBUGER){
			printf("round %d:\n",len-i+1);
			printArr(arr,len);
		}
	}
	if(DEBUGER){
		printf("bubble sort end...\n");
	}
}
/* 可再优化：二分插入 */
void insertion(int *arr, int len){
	if(DEBUGER){
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
		if(DEBUGER){
			printf("round %d:\n",j-1);
			printArr(arr,len);
		}
	}
	if(DEBUGER){
		printf("insertion sort end...\n");
	}
}

void quick(int *arr, int len){

}
void sub_merge(int *arr,int lp, int rp, int rb){
    int res[rb-lp+1];
    int mid = rp - 1;//左边界
    
    int i = lp,j=rp,k=0;//三个指针
    while(i <= mid && j <=rb) res[k++] = arr[i] < arr[j] ? arr[i++] : arr[j++];//合并从小到大
    while(i <=mid) res[k++] = arr[i++];//处理左边剩余
    while(j <=rb) res[k++] = arr[j++];//处理右边剩余
    
    for(i=0;i<rb-lp+1;i++) arr[lp + i] = res[i];//复制
}
void merge(int *arr, int left, int right){
    if(left == right) return;
    //分成两半
    int mid = left + (right - right)/2;//直接使用 (left + right)/2可能月姐
    //左边排序
    merge(arr, left, mid);
    //右边排序
    merge(arr, mid + 1, right);
    sub_merge(arr, left, mid+1, right);
}

void heap(int *arr, int len){

}
void shell(int *arr, int len){
    //计算gap 下一个gap = 3 * gap + 1,但长度不对大于len
    int h = 1;
    while(h <= len/3){
        h = 3*h + 1; //knuth序列 h = 3*h + 1
    }
	int gap,i,j,r=0;
	for(gap = h; gap > 0; gap = (gap-1)/3){
        r++;
        for(j = gap;j<len;j++){
            int key = arr[j];
            i = j - gap;
            while(i >=0 && arr[i] > key){
                arr[i+gap] = arr[i];
                i -= gap;
            }
            arr[i+gap] = key;
        }
		
		if(DEBUGER){
			printf("round %d:\n",r);
			printArr(arr,len);
		}
	}
}

void bucket(int *arr, int len){

}

/* 这种排序方式不稳定 */
void counting1(int *arr, int len,int max){
    int bucket[max];
    for(int i = 0;i<max;i++) bucket[i] = 0;
    
    for(int i = 0; i < len; i++){
        bucket[arr[i]]++;
    }
    int k = 0;
    for(int i = 0; i < max; i++){
        for(int j = 0; j < bucket[i]; j++){
            arr[k++] = i;
        }
    }
}

/* 特殊处理 让排序稳定 */
void counting(int *arr, int len,int max){
    int bucket[max];
    int ranked[len];
    for(int i = 0;i<max;i++) bucket[i] = 0;
    
    for(int i = 0; i < len; i++){
        bucket[arr[i]]++;//计数
    }
    
    for(int i = 1; i < max; i++){
        bucket[i] += bucket[i-1];//累加
    }
    
    for(int i = len -1; i >= 0; i--){
        ranked[--bucket[arr[i]]] = arr[i];//倒排
    }
    
    for(int i = 0; i < len; i++){
        arr[i] = ranked[i];//复制
    }
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

void testMergeSort(){
    int arr[15];
    int arr1[15];
    int len = 15;
    int max = 100;
    
    genArr(arr,len,max);
    memcpy(arr1,arr,sizeof(int)*len);
    
    printf("merge sort:\n");
    printf("\nsource:\n");
    printArr(arr,len);
    
    qsort(arr1,len,sizeof(int),comp);
    printf("\nsys:\n");
    printArr(arr1,len);

    printf("\nimpl:\n");
    merge(arr, 0, len-1);
    printArr(arr,len);

    //1.与确定正确的算法对比
    //2.为防止偶然，可使用足够多的随机样本，都成功则测试成功
    int ret = assertEqual(arr,arr1,len);
    printf("\nret:%d\n",ret);
    
}

void testCountingSort(){
    int arr[15];
    int arr1[15];
    int len = 15;
    int max = 100;
    
    genArr(arr,len,max);
    memcpy(arr1,arr,sizeof(int)*len);
    
    printf("counting sort:\n");
    printf("\nsource:\n");
    printArr(arr,len);
    
    qsort(arr1,len,sizeof(int),comp);
    printf("\nsys:\n");
    printArr(arr1,len);

    printf("\nimpl:\n");
    counting1(arr, len, max);
    printArr(arr,len);

    //1.与确定正确的算法对比
    //2.为防止偶然，可使用足够多的随机样本，都成功则测试成功
    int ret = assertEqual(arr,arr1,len);
    printf("\nret:%d\n",ret);
}

void testCounting1Sort(){
    int arr[15];
    int arr1[15];
    int len = 15;
    int max = 100;
    
    genArr(arr,len,max);
    memcpy(arr1,arr,sizeof(int)*len);
    
    printf("counting1 sort:\n");
    printf("\nsource:\n");
    printArr(arr,len);
    
    qsort(arr1,len,sizeof(int),comp);
    printf("\nsys:\n");
    printArr(arr1,len);

    printf("\nimpl:\n");
    counting(arr, len, max);
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

