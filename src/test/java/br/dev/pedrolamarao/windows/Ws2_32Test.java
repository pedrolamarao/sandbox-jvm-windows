package br.dev.pedrolamarao.windows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.NativeScope;

public final class Ws2_32Test
{
	@Test
	public void bind () throws Throwable
	{
		try (var scope = NativeScope.unboundedScope())
		{
			final var host = CLinker.toCString("localhost", scope);
			final var service = CLinker.toCString("http");
			final var hint = scope.allocate(Ws2_32.addrinfo.LAYOUT).fill((byte) 0);
			final var addressRef = scope.allocate(CLinker.C_POINTER, (long) 0);

			assertEquals(
				0, 
				(int) Ws2_32.getaddrinfo.invokeExact(host.address(), service.address(), hint.address(), addressRef.address())
			);
			
			final var address = MemoryAccess.getAddress(addressRef).asSegmentRestricted(Ws2_32.addrinfo.LAYOUT.byteSize());
			
			final var handle = (int) Ws2_32.socket.invokeExact(((int) Ws2_32.addrinfo.family.get(address)), ((int) Ws2_32.addrinfo.socktype.get(address)), ((int) Ws2_32.addrinfo.protocol.get(address)));
			assertNotEquals(-1, handle);
			
			assertEquals(
				0,
				(int) Ws2_32.bind.invokeExact(handle, MemoryAddress.ofLong((long) Ws2_32.addrinfo.addr.get(address)), (int) ((long) Ws2_32.addrinfo.addrlen.get(address)))
			);
			
			assertEquals(
				0,
				(int) Ws2_32.closesocket.invokeExact(handle)
			);
			
			Ws2_32.freeaddrinfo.invokeExact(address.address());
		}
	}
	
	@Test
	public void getaddrinfo () throws Throwable
	{
		try (var scope = NativeScope.unboundedScope())
		{
			final var host = CLinker.toCString("localhost", scope);
			final var service = CLinker.toCString("http");
			final var hint = scope.allocate(Ws2_32.addrinfo.LAYOUT).fill((byte) 0);
			final var addressRef = scope.allocate(CLinker.C_POINTER, (long) 0);

			assertEquals(
				0, 
				(int) Ws2_32.getaddrinfo.invokeExact(host.address(), service.address(), hint.address(), addressRef.address())
			);
			
			final var address = MemoryAccess.getAddress(addressRef);
			
			Ws2_32.freeaddrinfo.invokeExact(address.address());
		}
	}
	@Test
	public void listen () throws Throwable
	{
		try (var scope = NativeScope.unboundedScope())
		{
			final var host = CLinker.toCString("localhost", scope);
			final var service = CLinker.toCString("http");
			final var hint = scope.allocate(Ws2_32.addrinfo.LAYOUT).fill((byte) 0);
			final var addressRef = scope.allocate(CLinker.C_POINTER, (long) 0);

			assertEquals(
				0, 
				(int) Ws2_32.getaddrinfo.invokeExact(host.address(), service.address(), hint.address(), addressRef.address())
			);
			
			final var address = MemoryAccess.getAddress(addressRef).asSegmentRestricted(Ws2_32.addrinfo.LAYOUT.byteSize());
			
			final var handle = (int) Ws2_32.socket.invokeExact(((int) Ws2_32.addrinfo.family.get(address)), ((int) Ws2_32.addrinfo.socktype.get(address)), ((int) Ws2_32.addrinfo.protocol.get(address)));
			assertNotEquals(-1, handle);
			
			assertEquals(
				0,
				(int) Ws2_32.bind.invokeExact(handle, MemoryAddress.ofLong((long) Ws2_32.addrinfo.addr.get(address)), (int) ((long) Ws2_32.addrinfo.addrlen.get(address)))
			);
			
			assertEquals(
				0,
				(int) Ws2_32.listen.invokeExact(handle, 0)
			);
			
			assertEquals(
				0,
				(int) Ws2_32.closesocket.invokeExact(handle)
			);
			
			Ws2_32.freeaddrinfo.invokeExact(address.address());
		}
	}
	
	@Test
	public void socket () throws Throwable
	{
		final var handle = (int) Ws2_32.socket.invokeExact(Ws2_32.AF_INET, Ws2_32.SOCK_STREAM, Ws2_32.IPPROTO_TCP);
		assertNotEquals(-1, handle);
		final var r0 = (int) Ws2_32.closesocket.invokeExact(handle);
		assertNotEquals(-1, r0);
	}
}
