package br.dev.pedrolamarao.windows;

import static jdk.incubator.foreign.CLinker.C_INT;
import static jdk.incubator.foreign.CLinker.C_LONG;
import static jdk.incubator.foreign.CLinker.C_LONG_LONG;
import static jdk.incubator.foreign.CLinker.C_POINTER;
import static jdk.incubator.foreign.CLinker.C_SHORT;
import static jdk.incubator.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.LibraryLookup;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemoryHandles;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.ValueLayout;

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
	
	public static final int NI_NUMERICHOST = 0x02;
	
	public static final int NI_NUMERICSERV = 0x08;

	public static final int SO_DEBUG = 0x0001;

	public static final int SO_UPDATE_ACCEPT_CONTEXT = 0x700B;
	
	public static final int SOCK_STREAM = 1;

	public static final int SOCK_DGRAM = 2;
	
	public static final int SOL_SOCKET = 0xFFFF;
	
	public static final int WSA_IO_PENDING = 997;
	
	// types
	
	public static final class addrinfo
	{
		public static final GroupLayout LAYOUT = MemoryLayout.ofStruct(
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
	
	public static final class in_addr
	{
		public static final ValueLayout LAYOUT = MemoryLayout.ofValueBits(32, ByteOrder.BIG_ENDIAN);
	}
	
	public static final class in6_addr
	{
		public static final ValueLayout LAYOUT = MemoryLayout.ofValueBits(128, ByteOrder.BIG_ENDIAN);
	}
	
	public static final class sockaddr
	{
		public static final GroupLayout LAYOUT = MemoryLayout.ofStruct(
			C_SHORT.withName("family"),
			MemoryLayout.ofPaddingBits(112)
		);

		public static final VarHandle family = LAYOUT.varHandle(short.class, groupElement("family"));
	}
	
	public static final class sockaddr_in
	{
		public static final GroupLayout LAYOUT = MemoryLayout.ofStruct(
			C_SHORT.withName("family"),
			C_SHORT.withName("port"),
			in_addr.LAYOUT.withName("addr"),
			MemoryLayout.ofPaddingBits(64)
		);

		public static final VarHandle family = LAYOUT.varHandle(short.class, groupElement("family"));

		public static final VarHandle port;
		
		static {
			final var handle = MemoryHandles.varHandle(short.class, ByteOrder.BIG_ENDIAN);
			final var offset = LAYOUT.byteOffset(groupElement("port"));
			port = MemoryHandles.insertCoordinates(handle, 1, offset);			
		}
		
		public static final VarHandle addr = LAYOUT.varHandle(int.class, groupElement("addr"));
	}
	
	public static final class sockaddr_in6
	{
		public static final GroupLayout LAYOUT = MemoryLayout.ofStruct(
			C_SHORT.withName("family"),
			C_SHORT.withName("port"),
			C_LONG.withName("flowinfo"),
			in6_addr.LAYOUT.withName("addr"),
			C_LONG.withName("scope_id")
		);

		public static final VarHandle family = LAYOUT.varHandle(short.class, groupElement("family"));

		public static final VarHandle port;
		
		static {
			final var handle = MemoryHandles.varHandle(short.class, ByteOrder.BIG_ENDIAN);
			final var offset = LAYOUT.byteOffset(groupElement("port"));
			port = MemoryHandles.insertCoordinates(handle, 1, offset);			
		}

		public static final VarHandle flowInfo = LAYOUT.varHandle(int.class, groupElement("flowinfo"));

		public static final VarHandle scopeId = LAYOUT.varHandle(int.class, groupElement("scope_id"));
	}
	
	public static final class sockaddr_storage
	{
		public static final GroupLayout LAYOUT = MemoryLayout.ofStruct(
			C_SHORT.withName("family"),
			MemoryLayout.ofPaddingBits(1008)
		);

		public static final VarHandle family = LAYOUT.varHandle(short.class, groupElement("family"));
	}
	
	public static final class SOCKET
	{
		public static final ValueLayout LAYOUT = CLinker.C_LONG_LONG;
	}
	
	// methods
	
	public static final MethodHandle bind;
	
	public static final MethodHandle closesocket;
	
	public static final MethodHandle freeaddrinfo;
	
	public static final MethodHandle getaddrinfo;
	
	public static final MethodHandle getnameinfo;
	
	public static final MethodHandle getsockname;
	
	public static final MethodHandle listen;
	
	public static final MethodHandle socket;

	public static final MethodHandle setsockopt;
		
	public static final MethodHandle WSAGetLastError;
	
	public static final MethodHandle WSAGetOverlappedResult;
	
	static
	{
    	final var library = LibraryLookup.ofLibrary("Ws2_32");
    	
    	final var linker = CLinker.getInstance();

		bind = linker.downcallHandle(
			library.lookup("bind").get(),
			MethodType.methodType(int.class, MemoryAddress.class, MemoryAddress.class, int.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER, C_INT)
		);

		closesocket = linker.downcallHandle(
			library.lookup("closesocket").get(),
			MethodType.methodType(int.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_POINTER)
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
		
    	getnameinfo = linker.downcallHandle(
    		library.lookup("getnameinfo").get(),
			MethodType.methodType(int.class, MemoryAddress.class, int.class, MemoryAddress.class, int.class, MemoryAddress.class, int.class, int.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_INT, C_POINTER, C_INT, C_POINTER, C_INT, C_INT)
		);
		
    	getsockname = linker.downcallHandle(
    		library.lookup("getnameinfo").get(),
			MethodType.methodType(int.class, MemoryAddress.class, int.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_INT)
		);

		listen = linker.downcallHandle(
			library.lookup("listen").get(),
			MethodType.methodType(int.class, MemoryAddress.class, int.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_INT)
		);

		socket = linker.downcallHandle(
			library.lookup("socket").get(),
			MethodType.methodType(MemoryAddress.class, int.class, int.class, int.class),
			FunctionDescriptor.of(C_POINTER, C_INT, C_INT, C_INT)
		);

		setsockopt = linker.downcallHandle(
			library.lookup("setsockopt").get(),
			MethodType.methodType(int.class, MemoryAddress.class, int.class, int.class, MemoryAddress.class, int.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_INT, C_INT, C_POINTER, C_INT)
		);

		WSAGetLastError = linker.downcallHandle(
			library.lookup("WSAGetLastError").get(),
			MethodType.methodType(int.class),
			FunctionDescriptor.of(C_INT)
		);

		WSAGetOverlappedResult = linker.downcallHandle(
			library.lookup("WSAGetOverlappedResult").get(),
			MethodType.methodType(int.class, int.class, MemoryAddress.class, MemoryAddress.class, int.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_INT, C_POINTER, C_POINTER, C_INT, C_POINTER)
		);
	}
}
