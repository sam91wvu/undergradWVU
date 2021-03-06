#define COM1_INT_ID 0x0c
#define COM1_BASE 0x3F8
#define COM1_INT_EN COM1_BASE+1
#define COM1_BRD_LSB COM1_BASE
#define COM1_BRD_MSB COM1_BASE+1
#define COM1_INT_ID_REG COM1_BASE+2
#define COM1_LC COM1_BASE+3
#define COM1_MC COM1_BASE+4
#define COM1_LS COM1_BASE+5
#define COM1_MS COM1_BASE+6
#define PIC_MASK 0x21
#define PIC_CMD 0x20
#define EOI 0x20

#define IDLECOM 3
#define OPENCOM 1
#define CLOSEDCOM 0

#define WRITING 2
#define READING 4 

#define INPROGRESS 0
#define COMPLETE 1

typedef struct{
	int COM_OPEN;
	int* event_flag;
	int status;
	char* in_buff;
	int* in_count;
	int in_done;
	char* out_buff;
	int* out_count;
	int out_done;
	char ring_buffer[1024];
	int ring_buffer_in;
	int ring_buffer_out;
	int ring_buffer_count;
}DCB;


int com_open(int*,int);
int com_close(void);
int com_read(char*,int*);
int com_write(char*,int*);
void interrupt topLevel();
void writeInterrupt();
void readInterrupt();
void CreateDCB();