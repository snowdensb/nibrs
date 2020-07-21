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
package org.search.nibrs.stagingdata.repository;

import java.util.List;

import org.search.nibrs.stagingdata.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AgencyRepository extends JpaRepository<Agency, Integer>{
	public Agency findFirstByAgencyOri(String agencyOri);
	
	@Query("SELECT a from Agency a "
		+ "WHERE exists (select adminSegment from AdministrativeSegment adminSegment "
		+ "					where adminSegment.agency.agencyId = a.agencyId "
		+ "						  AND (?1 = null OR adminSegment.owner.ownerId = ?1 )) "
		+ "		OR exists (select arrestReportSegment from ArrestReportSegment arrestReportSegment "
		+ "					where arrestReportSegment.agency.agencyId = a.agencyId "
		+ "						  AND (?1 = null OR arrestReportSegment.owner.ownerId = ?1)) "
		+ "Order BY a.agencyName ")
	public List<Agency> findAllHavingData(Integer ownerId);
	
	@Query("SELECT distinct(a.agencyId) from Agency a "
			+ "WHERE exists (select adminSegment from AdministrativeSegment adminSegment "
			+ "					where adminSegment.agency.agencyId = a.agencyId "
			+ "						  AND (?1 = null OR adminSegment.owner.ownerId = ?1 )) "
			+ "		OR exists (select arrestReportSegment from ArrestReportSegment arrestReportSegment "
			+ "					where arrestReportSegment.agency.agencyId = a.agencyId "
			+ "						  AND (?1 = null OR arrestReportSegment.owner.ownerId = ?1)) "
			+ "Order BY a.agencyName ")
	public List<Integer> findAllAgencyIdsByOwner(Integer ownerId);
	
}
