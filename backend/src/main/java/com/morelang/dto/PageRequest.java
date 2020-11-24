package com.morelang.dto;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PageRequest {
	private int page;
	private int size;
	private Sort.Direction direction;
	public PageRequest() {}
	public PageRequest(int page, int size, Direction direction) {
		super();
		this.page = page;
		this.size = size;
		this.direction = direction;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page <=0 ? 1 : page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Sort.Direction getDirection() {
		return direction;
	}
	public void setDirection(Sort.Direction direction) {
		this.direction = direction;
	}
	public org.springframework.data.domain.PageRequest of() {
		return org.springframework.data.domain.PageRequest.of(page-1, size, direction, "scrapId");
	}
	public org.springframework.data.domain.PageRequest vocaof() {
		return org.springframework.data.domain.PageRequest.of(page-1, size, direction, "vocaId");
	}
	public org.springframework.data.domain.PageRequest videoTime() {
		return org.springframework.data.domain.PageRequest.of(page-1, size, direction, "viewDay");
	}
	public org.springframework.data.domain.PageRequest charge() {
		return org.springframework.data.domain.PageRequest.of(page-1, size, direction, "chargeTime");
	}
	public org.springframework.data.domain.PageRequest channelId() {
		return org.springframework.data.domain.PageRequest.of(page-1, size, direction, "id");
	}
	public org.springframework.data.domain.PageRequest channelCount() {
		return org.springframework.data.domain.PageRequest.of(page-1, size, direction, "count");
	}
}
