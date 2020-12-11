package br.dev.pedrolamarao.windows;

import static java.lang.invoke.MethodType.methodType;
import static jdk.incubator.foreign.CLinker.C_INT;
import static jdk.incubator.foreign.CLinker.C_POINTER;

import java.lang.invoke.MethodHandle;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.LibraryLookup;
import jdk.incubator.foreign.MemoryAddress;

public final class Mswsock
{
	// constants
	
	// types
	
	// methods
	
	public static final MethodHandle AcceptEx;
	
	static
	{
    	final var library = LibraryLookup.ofLibrary("Mswsock");
    	
    	final var linker = CLinker.getInstance();

		AcceptEx = linker.downcallHandle(
			library.lookup("AcceptEx").get(),
			methodType(int.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class, int.class, int.class, int.class, MemoryAddress.class, MemoryAddress.class),
			FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER, C_POINTER, C_INT, C_INT, C_INT, C_POINTER, C_POINTER)
		);
	}
}
