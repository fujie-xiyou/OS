//描述每一个空闲块的数据结构
struct free_block_type{
    int size;
    int start_addr;
    struct free_block_type *next;
}

//指向内存中空闲块链表的首指针
struct free_block_type *free_block;

struct allocatef_block{
    int pid;
    int size;
    int start_addr;
    char process_name[PROCESS_NAME_LEN];
    struct allocated_block *next;
}
struct alocated_block *allocated_block_head = NULL;

#define PROCESS_NAME_LEN 32 //进程名长度
#define MIN_SLICE 10 //最小碎片的大小
#define DEFAULT_MEM_SIZE 1024 //内存大小
#define DEFAULT_MEM_START 0 //起始位置


//内存分配算法
#define MA_FF 1
#define MA_BF 2
#define MA_WF 3

int mem_size = DEFAULT_MEM_SIZE; //内存大小
int ma_algorithm = MA_FF;   //当前分配算法
struct free_block_type init_free_block(int mem_size);
void display_memu();


int set_mem_size();
void set_algouthm();
void rearrange(int alogorithm);
//按FF算法重新整理内存空闲块链表
void rearrange_FF();

// 按BF算法重新整理内存空间链表
void rearrange_BF();

// 按WF算法重新整理内存空闲块链表
void rearrange_WF();

int new_process();

int allocate_mem(struct allocated_block *ab);

void kill_process();

int free_mem(struct allocated_block *ab);

int dispose(struct allocated_block *free_ab);

int display_mem_usage();

int main(){
    char choice;
    pid = 0;
    free_block = init_free_block(mem_size)
}
