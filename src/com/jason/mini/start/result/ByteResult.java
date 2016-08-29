package com.jason.mini.start.result;

public class ByteResult implements Result<byte[]>{

	private byte[] result;
	
	public ByteResult(byte[] result) {
		this.result = result;
	}
	
	@Override
	public String getViewName() {
		return "byte";
	}

	@Override
	public byte[] getResult() {
		return result;
	}

	@Override
	public int getBytesLength() {
		return result.length;
	}

}
