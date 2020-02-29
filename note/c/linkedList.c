#include <stdio.h>
#include <stdlib.h>

struct list{
	int data;//数据域
	struct list *next;//指针域
};

//遍历链表，参数为链表的首元素指针
void traverse(struct list *ls)//循环遍历链表
{
	struct list *p = ls;
	while(p){
		printf("%d\n",p->data);
		p = p->next;//指向它对应的下一个节点
	}
}

//创建一个节点
struct list *create_list(){
	return calloc(sizeof(struct list),1);
}

//在链表的指定位置插入一个元素
struct list *insert(struct list *ls,int index,int data){
	struct list *p = ls;
	while(p && index--){
		p = p->next;
	}
	if(p==NULL){
		return NULL;//表示n的位置大于链表的节点数
	}
	struct list *node = create_list();//新建立一个节点
	node->data = data;
	node->next = p->next;
	p->next=node;
}

//删除元素
int delete(struct list *ls,int index){
	struct list *p = ls;
	while(p && index--){
		p = p->next;
	}
	
	if(p == NULL){
		return -1;//index的位置不合适
	}
	
	struct list *tmp = p->next;
	p->next = p->next->next;
	free(tmp);
	return 0;//删除成功
}

//查询链表的个数
int size(struct list *ls){
	int i = 0;
	//struct list *p = ls;
	while(/*p*/ls){
		//p = p->next;
		ls = ls->next;
		i++;
	}
	return i;
}

//清空链表
void clear(struct list *ls){
	struct list *p = ls->next;
	while(p){
		struct list *tmp = p->next;
		free(p);
		p = tmp;
	}
	ls->next = NULL;
}

//判断链表是否为空
int isEmpty(struct list *ls){
	if(ls->next){
		return 0;
	}else{
		return -1;
	}
}

//返回链表指定位置的节点
struct list *getList(struct list *ls,int n){
	struct list *p = ls;
	while(p && n--){
		p = p->next;
	}
	if(p == NULL)
		return NULL;
	return p;
}
//返回数据域等于data的节点TODO
//返回数据域等于data的节点的位置TODO
//得到链表的最后一个节点TODO
//合并两个链表，结果放入到第一个链表，不合并链表头，只要车箱不要车头

int main(void){
	struct list *first = calloc(sizeof(struct list),1);//在堆空间创建一个节点
	struct list *second = calloc(sizeof(struct list),1);//在堆空间创建一个节点
	struct list *third = calloc(sizeof(struct list),1);//在堆空间创建一个节点
	first->next = second;
	second->next=third;
	third->next = NULL;//对于链表的最后一个节点，next一定为NULL
	first->data = 1;
	second->data = 2;
	third->data = 3;
	
	//insert(first,1,10);
	//delete(first,0);
	//clear(first);
	traverse(first);
	printf("data:%d\n",getList(first,0)->data);
	printf("count:%d\n",isEmpty(first));
	return 0;
}