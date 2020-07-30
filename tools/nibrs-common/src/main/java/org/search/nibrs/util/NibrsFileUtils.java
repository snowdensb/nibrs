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
package org.search.nibrs.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;

/**
 * Utilities class for getting file content type
 *
 */
public class NibrsFileUtils {
	static Tika defaultTika = new Tika();
	
	public static final String getMediaType(InputStream inStream) throws IOException {
		return defaultTika.detect(inStream);
	}
	
	public static final String getMediaType(InputStream inStream, String fileName) throws IOException {
		return defaultTika.detect(inStream, fileName);
	}
	
	public static final String getMediaType(File file) throws IOException {
		return defaultTika.detect(file);
	}
	
}

