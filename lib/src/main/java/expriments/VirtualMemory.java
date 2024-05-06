package expriments;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.TreeMap;



public class VirtualMemory {
    private MappedByteBuffer buffer;
    private SpinLock lock;

    public VirtualMemory(String fileName, int size) throws Exception {
        try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
			FileChannel channel = file.getChannel();
			this.buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, size);
		}
        this.lock = new SpinLock(buffer);
    }
    
    public VirtualMemory(File fileName, int size) throws Exception {
        try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
			FileChannel channel = file.getChannel();
			this.buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, size);
		}
        this.lock = new SpinLock(buffer);
    }

    public int getCount() {
        return buffer.getInt(4);  // 카운터 값을 4번째 바이트 위치에서 읽음
    }

    public void incrementCount() {
        lock.tryLock(1000);  // 1초 동안 락 시도
        try {
            int count = getCount();
            buffer.putInt(4, count + 1);  // 카운터 값을 4번째 바이트 위치에 저장
        } finally {
            lock.unlock();  // 작업 완료 후 락 해제
        }
    }

    public static void main(String[] args) throws Exception {
        
    	File file = new File("shared_memory.bin");
    	
    	VirtualMemory virtualMemory = new VirtualMemory(file, 8); // 버퍼 크기 조정
        
        //VirtualMemory virtualMemory = new VirtualMemory("shared_memory.bin", 8); // 버퍼 크기 조정
        for (int i = 0; i < 10000000; i++) {
            virtualMemory.incrementCount();
            System.out.println("Count: " + virtualMemory.getCount());
        }
    }
}