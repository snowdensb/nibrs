/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.stagingdata.model.search;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SearchResult<T> implements Serializable {
	private static final long serialVersionUID = 7916910066665545067L;

	private Integer totalCount; 
	private Integer returnedCount; 
	private List<T> returnedHits; 
	public SearchResult() {
		super();
	}
	
	public SearchResult(List<T> searchHits, Integer sizeLimit) {
		this();
		this.setTotalCount(searchHits.size());
		
		
		if (getTotalCount() > sizeLimit) {
			this.setReturnedHits(searchHits.stream()
					.limit(sizeLimit)
					.collect(Collectors.toList()));
			this.setReturnedCount(sizeLimit); 
		}
		else {
			this.setReturnedHits(searchHits);
			this.setReturnedCount(this.getTotalCount()); 
		}
		
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public Integer getReturnedCount() {
		return returnedCount;
	}

	private void setReturnedCount(Integer returnedCount) {
		this.returnedCount = returnedCount;
	}


	public Integer getTotalCount() {
		return totalCount;
	}

	private void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getReturnedHits() {
		return returnedHits;
	}

	public void setReturnedHits(List<T> returnedHits) {
		this.returnedHits = returnedHits;
	}
}
