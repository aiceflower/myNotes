#include <stdio.h>
#include <stdlib.h>

struct list{
	int data;//������
	struct list *next;//ָ����
};

//������������Ϊ�������Ԫ��ָ��
void traverse(struct list *ls)//ѭ����������
{
	struct list *p = ls;
	while(p){
		printf("%d\n",p->data);
		p = p->next;//ָ������Ӧ����һ���ڵ�
	}
}

//����һ���ڵ�
struct list *create_list(){
	return calloc(sizeof(struct list),1);
}

//�������ָ��λ�ò���һ��Ԫ��
struct list *insert(struct list *ls,int index,int data){
	struct list *p = ls;
	while(p && index--){
		p = p->next;
	}
	if(p==NULL){
		return NULL;//��ʾn��λ�ô�������Ľڵ���
	}
	struct list *node = create_list();//�½���һ���ڵ�
	node->data = data;
	node->next = p->next;
	p->next=node;
}

//ɾ��Ԫ��
int delete(struct list *ls,int index){
	struct list *p = ls;
	while(p && index--){
		p = p->next;
	}
	
	if(p == NULL){
		return -1;//index��λ�ò�����
	}
	
	struct list *tmp = p->next;
	p->next = p->next->next;
	free(tmp);
	return 0;//ɾ���ɹ�
}

//��ѯ����ĸ���
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

//�������
void clear(struct list *ls){
	struct list *p = ls->next;
	while(p){
		struct list *tmp = p->next;
		free(p);
		p = tmp;
	}
	ls->next = NULL;
}

//�ж������Ƿ�Ϊ��
int isEmpty(struct list *ls){
	if(ls->next){
		return 0;
	}else{
		return -1;
	}
}

//��������ָ��λ�õĽڵ�
struct list *getList(struct list *ls,int n){
	struct list *p = ls;
	while(p && n--){
		p = p->next;
	}
	if(p == NULL)
		return NULL;
	return p;
}
//�������������data�Ľڵ�TODO
//�������������data�Ľڵ��λ��TODO
//�õ���������һ���ڵ�TODO
//�ϲ���������������뵽��һ���������ϲ�����ͷ��ֻҪ���䲻Ҫ��ͷ

int main(void){
	struct list *first = calloc(sizeof(struct list),1);//�ڶѿռ䴴��һ���ڵ�
	struct list *second = calloc(sizeof(struct list),1);//�ڶѿռ䴴��һ���ڵ�
	struct list *third = calloc(sizeof(struct list),1);//�ڶѿռ䴴��һ���ڵ�
	first->next = second;
	second->next=third;
	third->next = NULL;//������������һ���ڵ㣬nextһ��ΪNULL
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