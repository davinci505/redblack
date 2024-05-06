package expriments;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;

import sun.misc.Unsafe;

public class SpinLock {
    private static Unsafe unsafe = null;
    
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        }catch(Exception e){
        	
        }
    }
    
    private final long addr;

    public SpinLock(MappedByteBuffer shm) {
    	
    	 try {
             Class<?> cls = shm.getClass();
             Method maddr = cls.getMethod("address");
             maddr.setAccessible(true);
             Long addr = (Long) maddr.invoke(shm);
             if (addr == null) {
                 throw new RuntimeException("Unable to retrieve buffer's address");
             }
             this.addr = addr;
         } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
             throw new RuntimeException(ex);
         }
    	
       
    }

    public boolean tryLock(long maxWait) {
        long deadline = System.currentTimeMillis() + maxWait;
        while (System.currentTimeMillis() < deadline ) {
            if (unsafe.compareAndSwapInt(null, addr, 0, 1)) {
                return true;
            }
            System.out.println("yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~yield~~");
        }
        return false;
    }

    public void unlock() {
        unsafe.putInt(addr, 0);
    }
}
//public class SpinLock {
//    private static final VarHandle INT_HANDLE;
//    static {
//        try {
//            INT_HANDLE = MethodHandles.byteBufferViewVarHandle(int[].class, ByteOrder.nativeOrder());
//        } catch (Exception e) {
//            throw new RuntimeException("VarHandle 초기화 실패", e);
//        }
//    }
//
//    private final ByteBuffer buffer;
//
//    public SpinLock(ByteBuffer buffer) {
//        this.buffer = buffer;
//    }
//
//    public boolean tryLock(long maxWait) {
//        long startTime = System.nanoTime();
//        long deadline = startTime + maxWait * 1_000_000L; // maxWait을 나노초로 변환
//        while (System.nanoTime() < deadline) {
//            int currentValue = (int) INT_HANDLE.getVolatile(buffer, 0);
//            if (currentValue == 0 && INT_HANDLE.compareAndSet(buffer, 0, 0, 1)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void unlock() {
//        INT_HANDLE.setVolatile(buffer, 0, 0);
//    }
//}
