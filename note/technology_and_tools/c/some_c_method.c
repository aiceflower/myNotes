#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#pragma warning(disable:4996)
//求字符串的长度
int mystrlen(char s[]);
//去掉字符串的左边空格
int lTrim(char arr[]);
//去掉字符串的右边空格
int rTrim(char arr[]);
//去掉字符串的两端空格
int trim(char arr[]);
//将字符串转换为int
int strtoint(char arr[]);
//将int转换成字符串
int inttostr(int n,char s[]);
//颠倒字符串的顺序
int revstr(char arr[]);
//十进制转换为二进制
int dtobin(int a,char c[]);
//把int转换成ip
void ip2s(int n);
//把ip转换成int
int s2ip(char s[]);
//使用指针计算数组的长度
int plen(char s[]);
//用指针合并两个数组并将结果放入到第一个数组中
int mycat(char s1[],char s2[]);

//用指针写冒泡排序
int bubble(int *p,int n);

//二分查找
int halfsearch(int arr[],int min,int max,int key);

//常规方法作二分查找
int hs(int arr[],int min,int max,int key);
//需要将函数定义在main函数前面，或者申明在前面，否则C语言会报错。

//打印int数组
void printarr(const int arr[],int n);


//复制文件
void cFile(char res[],char des[]);

char *mystr(char *s,char c){
	while(*s){
		if(*++s==c){
			return s;
		}
		return NULL;
	}
}

//结构体
struct man{
	char *name;
	int age;
};

int main(){
	
}
//求字符串的长度
int mystrlen(char s[]){
	int len = 0;
	while(s[len++]);
	len--;
	return len;
}

//去掉字符串的左边空格
int lTrim(char arr[]){
	int len = 0;
	while(arr[len++]==' ');len--;//计算字符数组前面有几个空格
	int i = 0;
	while(arr[i]){
		arr[i-len]=arr[i];
		i++;
	}
	arr[i-len]=0;//将字符串最后补0
	printf("(%s)\n",arr);
	return 0;
}

//去掉字符串的右边空格
int rTrim(char arr[]){
	int len = 0;
	while(arr[len++]);len--;//计算字符数组的长度	
	int i = 0;
	for(i = len-1;i>0;--i){
		if(arr[i]!=' '){
			arr[i+1]=0;
			break;
		}
	}
	printf("(%s)\n",arr);
	return 0;
}

//去掉字符串的两端空格
int trim(char arr[]){
	lTrim(arr);
	rTrim(arr);
}

//将字符串转换为int
int strtoint(char arr[]){
	int len =0;
	while(arr[len++]);
	len --;
	int i = 0;
	int result = 0;
	for(i = 0;i<len;++i){
		int j = 0;
		int k = 1;
		for(j=len-i-1;j>0;j--){
			k*=10;
		}
		result += (arr[i]-'0')*k;
	}
	return result;
}

//将int转换成字符串
int inttostr(int n,char s[]){//这里没有考虑负数的情况
	int i = 0;
	while(n){
		int tmp = n%10;
		char c = tmp+'0';
		s[i]=c;
		++i;
		n /=10;
	}
	revstr(s);
}

//颠倒字符串的顺序
int revstr(char arr[]){
	int min = 0;
	int max = strlen(arr)-1;
	while(min<max){
		char temp = arr[min];
		arr[min] = arr[max];
		arr[max] = temp;
		min++;
		max--;
	}
}

//十进制转换为二进制
int dtobin(int a,char s[]){
	int i = a%2;
	if(a>0){
		dtobin(a/2,s);
		char c[100]={0}	 ;
		sprintf(c,"%d",i);
		strcat(s,c);
	}
}

//把int转换成ip
void ip2s(int n){
	unsigned char *p = &n;
	printf("%u.%u.%u.%u\n",*p,*(p+1),*(p+2),*(p+3));
}
//把ip转换成int
int s2ip(char s[]){
	
	int a;
	int b;
	int c;
	int d;
	sscanf(s,"%d.%d.%d.%d",&a,&b,&c,&d);
	//printf("a=%d,b=%d,c=%d,d=%d",a,b,c,d);
	int ip;
	char *p = &ip;
	*p = a;
	p++;
	*p = b;
	p++;
	*p = c;
	p++;
	*p = d;
	return ip;
}

//使用指针计算数组的长度
int plen(char s[]){
	char *p = s;
	int i = 0;
	while(*p){
		i++;
		p++;
	}
	return i;
}

//用指针合并两个数组并将结果放入到第一个数组中
int mycat(char s[],char c[]){
	char *ps = s;
	while(*ps){
		ps++;
	}
	char *pc = c;
	while(*pc){
		*ps++ = *pc++;
	}
	printf("s1=%s",s);
}
//用指针求数组中最大的元素

int pmax(int *s){
	int max = *s;
	int j = 0;
	for(j=0;j<5;j++){
		if(max<*(s+j)){
			max = *(s+j);
		}
	}
	int sec = *s;
	for(j=0;j<5;j++){
		if(sec<*(s+j)&&(*(s+j)!=max)){
			sec = *(s+j);
		}
	}
	return sec;
}

//用指针写冒泡排序
int bubble(int *p,int n){
	int i=0;
	for(i=0;i<n;++i){
		int j = 0;
		for(j=0;j<n-i-1;++j){
			if( *(p+j) > *(p+j+1) ){
				int tmp = *(p+j);
				*(p+j)=*(p+j+1);
				*(p+j+1) = tmp;
			}
		}
	}
}
//二分查找
int halfsearch(int arr[],int min,int max,int key){
	if(min<=max){
		int mid = (min+max)/2;
		if(key>arr[mid]){
			return halfsearch(arr,mid+1,max,key);
		}else if(key<arr[mid]){
			return halfsearch(arr,min,mid-1,key);
		}else{
			return mid;
		}
	}else{
		return -1;
	}
}

//打印int数组
void printarr(const int arr[],int n){
	for(int i = 0;i<n;++i){
		if(i==0){
			printf("[%d,",arr[i]);
		}else if(i==(n-1)){
			printf("%d]\n",arr[i]);
		}else{
			printf("%d,",arr[i]);
		}
	}
}

//常规方法作二分查找
int hs(int arr[],int min,int max,int key){
	int mid = min;
	while(min<=max){
		mid = (min+max)/2;
		printf("%d...%d....%d\n",key,mid,arr[mid]);
		if(key==arr[mid]){
			return mid;
		}else if(key>arr[mid]){
			min = mid+1;
		}else{
			max = mid-1;
		}
	}
	return -1;
}
//复制文件
void cFile(char *res,char *des){
	FILE *r = fopen(res,"r");
	FILE *w = fopen(des,"w");
	fseek(r,0,SEEK_END);
	long int a = ftell(r);
	rewind(r);
	char *c = malloc(a);
	fread(c,a,1,r);
	fwrite(c,a,1,w);
	printf("end");
	fclose(r);
	fclose(w);
}