package br.dev.pedrolamarao.windows;

import static jdk.incubator.foreign.CLinker.C_INT;
import static jdk.incubator.foreign.CLinker.C_LONG_LONG;
import static jdk.incubator.foreign.CLinker.C_POINTER;
import static jdk.incubator.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.LibraryLookup;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemoryLayout;

public final class Ws2_32
{
	// constants
	
	public static final int AF_UNSPEC = 0;

	public static final int AF_INET = 2;

	public static final int AF_INET6 = 23;
	
	public static final int AI_PASSIVE = 0x01;
	
	public static final int INVALID_SOCKET = 0xFFFFFFFF;

	public static final int IPPROTO_ICMP = 1;

	public static final int IPPROTO_TCP = 6;

	public static final int IPPROTO_UDP = 17;

	public static final int SOCK_STREAM = 1;

	public static final int SOCK_DGRAM = 2;
	
	// types
	
	public static final class addrinfo
	{
		public static final MemoryLayout LAYOUT = MemoryLayout.ofStruct(
			C_INT.withName("flags"),
			C_INT.withName("family"),
			C_INT.withName("socktype"),
			C_INT.withName("protocol"),
			C_LONG_LONG.withName("addrlen"),
			C_POINTER.withName("canonname"),
			C_POINTER.withName("addr"),
			C_POINTER.withName("next")
		);
		
		public static final VarHandle flags = LAYOUT.varHandle(int.class, groupElement("flags"));
		
		public static final VarHandle family = LAYOUT.varHandle(int.class, groupElement("family"));
		
		public static final VarHandle socktype = LAYOUT.varHandle(int.class, groupElement("socktype"));
		
		public static final VarHandle protocol = LAYOUT.varHandle(int.class, groupElement("protocol"));
		
		public static final VarHandle addrlen = LAYOUT.varHandle(long.class, groupElement("addrlen"));
		
		public static final VarHandle addr = LAYOUT.varHandle(long.class, groupElement("addr"));
	}
	
	// methods
	
	public static final MethodHandle bind;
	
	public static final MethodHandle closesocket;
	
	public static final MethodHandle freeaddrinfo;
	
	public static final MethodHandle getaddrinfo;
	
	public static final MethodHandle listen;
	
	public static final MethodHandle socket;
	
	static
	{
    	final var library = LibraryLookup.ofLibrary("Ws2_32");
    	
    	final var linker = CLinker.getInstance();

		bind = linker.downcallHandle(
			library.lookup("bind").get(),
			MethodType.methodType(int.class, int.class, MemoryAddress.class, int.class),
			FunctionDescriptor.of(C_INT, C_INT, C_POINTER, C_INT)
		);

		closesocket = linker.downcallHandle(
			library.lookup("closesocket").get(),
			MethodType.methodType(int.class, int.class),
			FunctionDescriptor.of(C_INT, C_INT)
		);

    	freeaddrinfo = linker.downcallHandle(
    		library.lookup("freeaddrinfo").get(),
			MethodType.methodType(void.class, MemoryAddress.class),
			FunctionDescriptor.ofVoid(C_POINTER)
		);
		
    	getaddrinfo = linker.downcallHandle(
    		library.lookup("getaddrinfo").get(),
			MethodType.methodType(int.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER, C_POINTER, C_POINTER)
		);

		listen = linker.downcallHandle(
			library.lookup("listen").get(),
			MethodType.methodType(int.class, int.class, int.class),
			FunctionDescriptor.of(C_INT, C_INT, C_INT)
		);

		socket = linker.downcallHandle(
			library.lookup("socket").get(),
			MethodType.methodType(int.class, int.class, int.class, int.class),
			FunctionDescriptor.of(C_INT, C_INT, C_INT, C_INT)
		);
	}
}
