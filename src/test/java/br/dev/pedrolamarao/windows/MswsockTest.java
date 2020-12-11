package br.dev.pedrolamarao.windows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.NativeScope;

public final class MswsockTest
{
	@Test
	public void listen () throws Throwable
	{
		try (var scope = NativeScope.unboundedScope())
		{
			final var address = scope.allocate(Ws2_32.sockaddr_in.LAYOUT);
			address.fill((byte) 0);
			Ws2_32.sockaddr_in.family.set(address, (short) Ws2_32.AF_INET);
			
			final var port = (MemoryAddress) Ws2_32.socket.invokeExact(Ws2_32.AF_INET, Ws2_32.SOCK_STREAM, Ws2_32.IPPROTO_TCP);
			assertNotEquals(-1, port);
			
			assertEquals(
				0,
				(int) Ws2_32.bind.invokeExact(port, address.address(), (int) address.byteSize())
			);
			
			assertEquals(
				0,
				(int) Ws2_32.listen.invokeExact(port, 0)
			);
			
			final var link = (MemoryAddress) Ws2_32.socket.invokeExact(Ws2_32.AF_INET, Ws2_32.SOCK_STREAM, Ws2_32.IPPROTO_TCP);
			assertNotEquals(-1, link);
			
			final var operation = scope.allocate(Kernel32.OVERLAPPED.LAYOUT).fill((byte) 0);
			final var buffer = scope.allocate(2048);
			final var read = scope.allocate(CLinker.C_INT);
			
			assertEquals(
				0,
				(int) Mswsock.AcceptEx.invokeExact(port, link, buffer.address(), 0, 1024, 1024, read.address(), operation.address())
			);
				
			assertEquals(
				Ws2_32.WSA_IO_PENDING,
				(int) Ws2_32.WSAGetLastError.invokeExact()
			);
			
			assertEquals(
				0,
				(int) Ws2_32.closesocket.invokeExact(link)
			);

			
			assertEquals(
				0,
				(int) Ws2_32.closesocket.invokeExact(port)
			);
		}
	}
}
