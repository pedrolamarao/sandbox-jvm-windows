package br.dev.pedrolamarao.windows;

import static java.lang.invoke.MethodType.methodType;
import static jdk.incubator.foreign.CLinker.C_CHAR;
import static jdk.incubator.foreign.CLinker.C_INT;
import static jdk.incubator.foreign.CLinker.C_LONG_LONG;
import static jdk.incubator.foreign.CLinker.C_POINTER;
import static jdk.incubator.foreign.MemoryLayout.ofStruct;
import static jdk.incubator.foreign.MemoryLayout.ofUnion;
import static jdk.incubator.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.LibraryLookup;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemoryLayout;

public final class Kernel32
{
	// constants 
	
	public static final int ERROR_IO_INCOMPLETE = 996;
	
	public static final int ERROR_IO_PENDING = 997;
	
	public static final int FILE_FLAG_BACKUP_SEMANTICS = 0x02000000;
	
	public static final int FILE_FLAG_OVERLAPPED = 0x40000000;
	
	public static final int FILE_NOTIFY_CHANGE_FILE_NAME = 0x00000001;
    
    public static final int FILE_SHARE_DELETE = 0x00000004;
    
    public static final int FILE_SHARE_READ = 0x00000001;

    public static final int FILE_SHARE_WRITE = 0x00000002;

    public static final int GENERIC_READ = 0x80000000;

    public static final MemoryAddress INVALID_HANDLE_VALUE = MemoryAddress.ofLong(-1);
	
	public static final int OPEN_EXISTING = 3; 
	
    public static final int WAIT_TIMEOUT = 258;
    
    // types
    
    public static final class FILE_NOTIFY_INFORMATION 
    {
    	public static final GroupLayout LAYOUT = MemoryLayout.ofStruct(
			C_INT.withName("next"),
			C_INT.withName("action"),
			C_INT.withName("length"),
			C_CHAR.withName("name")
		)
		.withBitAlignment(Integer.SIZE);
    	
    	public static final VarHandle next = LAYOUT.varHandle(int.class, groupElement("next"));
    	
    	public static final VarHandle action = LAYOUT.varHandle(int.class, groupElement("action"));
    	
    	public static final VarHandle length = LAYOUT.varHandle(int.class, groupElement("length"));
    }
    
    public static final class OVERLAPPED
    {
    	public static final GroupLayout LAYOUT = ofStruct(
			C_POINTER, 
			C_POINTER, 
			ofUnion(
				ofStruct(
					C_INT.withName("offsetLow"),
					C_INT.withName("offsetHigh")
				),
				C_LONG_LONG.withName("offset"),
				C_POINTER.withName("pointer")
			).withName("union"),
			C_POINTER.withName("event")
		);
    	
    	public static final VarHandle offset = LAYOUT.varHandle(long.class, groupElement("union"), groupElement("offset"));
    }
    
    public enum READ_DIRECTORY_NOTIFY_INFORMATION_CLASS
    {
    	ReadDirectoryNotifyInformation(0),
    	ReadDirectoryNotifyExtendedInformation(1);
    	
    	int value;
    	
    	READ_DIRECTORY_NOTIFY_INFORMATION_CLASS (int value)
    	{
    		this.value = value;
    	}
    	
    	public int value ()
    	{
    		return value;
    	}
    }
    
    // methods
	
	public static final MethodHandle cancelIoEx;
	
	public static final MethodHandle closeHandle;
	
	public static final MethodHandle createFileA;

	public static final MethodHandle createIoCompletionPort;

	public static final MethodHandle getLastError;
	
	public static final MethodHandle getOverlappedResultEx;
	
	public static final MethodHandle getQueuedCompletionStatus;
	
	public static final MethodHandle lockFileEx;
	
	public static final MethodHandle postQueuedCompletionStatus;

	public static final MethodHandle readDirectoryChangesW;
	
	public static final MethodHandle readDirectoryChangesExW;
	
	public static final MethodHandle readFile;
	
	public static final MethodHandle readFileEx;

	static
	{
    	final var kernel32 = LibraryLookup.ofLibrary("Kernel32");
    	
    	final var linker = CLinker.getInstance();

		cancelIoEx = linker.downcallHandle(
			kernel32.lookup("CancelIoEx").get(),
			methodType(int.class, MemoryAddress.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER)
		);

		closeHandle = linker.downcallHandle(
			kernel32.lookup("CloseHandle").get(),
			MethodType.methodType(int.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_POINTER)
		);
    	
		createFileA = linker.downcallHandle(
			kernel32.lookup("CreateFileA").get(),
			MethodType.methodType(MemoryAddress.class, MemoryAddress.class, int.class, int.class, MemoryAddress.class, int.class, int.class, MemoryAddress.class),
			FunctionDescriptor.of(C_POINTER, C_POINTER, C_INT, C_INT, C_POINTER, C_INT, C_INT, C_POINTER)
		);

		createIoCompletionPort = linker.downcallHandle(
			kernel32.lookup("CreateIoCompletionPort").get(),
			methodType(MemoryAddress.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class, int.class),
			FunctionDescriptor.of(C_POINTER, C_POINTER, C_POINTER, C_POINTER, C_INT)
		);
		
		getLastError = linker.downcallHandle(
			kernel32.lookup("GetLastError").get(),
			MethodType.methodType(int.class),
			FunctionDescriptor.of(C_INT)
		);
		
		getOverlappedResultEx = linker.downcallHandle(
			kernel32.lookup("GetOverlappedResultEx").get(),
			methodType(int.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class, int.class, int.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER, C_POINTER, C_INT, C_INT)
		);

		getQueuedCompletionStatus = linker.downcallHandle(
			kernel32.lookup("GetQueuedCompletionStatus").get(),
			methodType(int.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class, int.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER, C_POINTER, C_POINTER, C_INT)
		);

		lockFileEx = linker.downcallHandle(
			kernel32.lookup("LockFileEx").get(),
			methodType(int.class, MemoryAddress.class, int.class, int.class, int.class, int.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_INT, C_INT, C_INT, C_INT, C_POINTER)
		);
		
		postQueuedCompletionStatus = linker.downcallHandle(
			kernel32.lookup("PostQueuedCompletionStatus").get(),
			methodType(int.class, MemoryAddress.class, int.class, MemoryAddress.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_INT, C_POINTER, C_POINTER)
		);
		
		readDirectoryChangesW = linker.downcallHandle(
			kernel32.lookup("ReadDirectoryChangesW").get(),
			methodType(int.class, MemoryAddress.class, MemoryAddress.class, int.class, int.class, int.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER, C_INT, C_INT, C_INT, C_POINTER, C_POINTER, C_POINTER)
		);
		
		readDirectoryChangesExW = linker.downcallHandle(
			kernel32.lookup("ReadDirectoryChangesExW").get(),
			methodType(int.class, MemoryAddress.class, MemoryAddress.class, int.class, int.class, int.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class, int.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER, C_INT, C_INT, C_INT, C_POINTER, C_POINTER, C_POINTER, C_INT)
		);
		
		readFile = linker.downcallHandle(
			kernel32.lookup("ReadFile").get(),
			methodType(int.class, MemoryAddress.class, MemoryAddress.class, int.class, MemoryAddress.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER, C_INT, C_POINTER, C_POINTER)
		);
		
		readFileEx = linker.downcallHandle(
			kernel32.lookup("ReadFileEx").get(),
			methodType(int.class, MemoryAddress.class, MemoryAddress.class, int.class, MemoryAddress.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER, C_INT, C_POINTER, C_POINTER)
		);
	}
}
