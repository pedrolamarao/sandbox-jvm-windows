package br.dev.pedrolamarao.windows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
	public void getnameinfo__sockaddr_in () throws Throwable
	{
		try (var scope = NativeScope.unboundedScope())
		{
			final var address = scope.allocate(Ws2_32.sockaddr_in.LAYOUT);
			final var host = scope.allocate(1024);
			final var service = scope.allocate(1024);
			
			address.fill((byte) 0);
			host.fill((byte) 0);
			service.fill((byte) 0);
			
			Ws2_32.sockaddr_in.family.set(address, (short) Ws2_32.AF_INET);
			final var r0 = (int) Ws2_32.getnameinfo.invokeExact(address.address(), (int) address.byteSize(), host.address(), (int) host.byteSize(), service.address(), (int) service.byteSize(), (int) (Ws2_32.NI_NUMERICHOST | Ws2_32.NI_NUMERICSERV));

			assertEquals(0, r0);
			assertEquals("0.0.0.0", CLinker.toJavaString(host));
			assertEquals("0", CLinker.toJavaString(service));
			
			address.fill((byte) 0);
			host.fill((byte) 0);
			service.fill((byte) 0);
			
			Ws2_32.sockaddr_in.family.set(address, (short) Ws2_32.AF_INET);
			Ws2_32.sockaddr_in.port.set(address, networkShort((short) 80));
			final var r1 = (int) Ws2_32.getnameinfo.invokeExact(address.address(), (int) address.byteSize(), host.address(), (int) host.byteSize(), service.address(), (int) service.byteSize(), (int) (Ws2_32.NI_NUMERICHOST | Ws2_32.NI_NUMERICSERV));

			assertEquals(0, r1);
			assertEquals("0.0.0.0", CLinker.toJavaString(host));
			assertEquals("80", CLinker.toJavaString(service));
			
			address.fill((byte) 0);
			host.fill((byte) 0);
			service.fill((byte) 0);
			
			Ws2_32.sockaddr_in.family.set(address, (short) Ws2_32.AF_INET);
			Ws2_32.sockaddr_in.port.set(address, networkShort((short) 443));
			Ws2_32.sockaddr_in.addr.set(address, networkInt(1));
			final var r2 = (int) Ws2_32.getnameinfo.invokeExact(address.address(), (int) address.byteSize(), host.address(), (int) host.byteSize(), service.address(), (int) service.byteSize(), (int) (Ws2_32.NI_NUMERICHOST | Ws2_32.NI_NUMERICSERV));

			assertEquals(0, r2);
			assertEquals("0.0.0.1", CLinker.toJavaString(host));
			assertEquals("443", CLinker.toJavaString(service));
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
	
	public static short networkShort (short value)
	{
		return ByteBuffer.allocate(Short.BYTES).order(ByteOrder.LITTLE_ENDIAN).putShort(0, value).order(ByteOrder.BIG_ENDIAN).getShort(0);
	}
	
	public static int networkInt (int value)
	{
		return ByteBuffer.allocate(Integer.BYTES).order(ByteOrder.LITTLE_ENDIAN).putInt(0, value).order(ByteOrder.BIG_ENDIAN).getInt(0);
	}
}
