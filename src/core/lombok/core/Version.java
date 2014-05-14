/*
 * Copyright (C) 2009-2014 The Project Lombok Authors.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package lombok.core;

/**
 * This class just holds hrisey's current version.
 */
public class Version {
	private static final String VERSION = "0.1.3-SNAPSHOT";
	private static final String RELEASE_NAME = "Parcellus Minor";
	
	private Version() {
		//Prevent instantiation
	}
	
	/**
	 * Prints the version followed by a newline, and exits.
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			System.out.printf("Hrisey %s\n", getFullVersion());
		} else {
			System.out.println(VERSION);
		}
	}
	
	/**
	 * Get the current Lombok version.
	 */
	public static String getVersion() {
		return VERSION;
	}
	
	/**
	 * Get the current release name.
	 * 
	 * The release name is a string (not numbers). Every time a new release has a significantly improved feature set, a new release name is given.
	 * Thus, many versions can carry the same release name. Version bumps and release names are not related; if a new version of lombok is entirely
	 * backwards compatible with a previous one, but also adds many new features, it will get only a minor version bump, but also a new release name.
	 */
	public static String getReleaseName() {
		return RELEASE_NAME;
	}
	
	public static String getFullVersion() {
		return String.format("v%s \"%s\"", VERSION, RELEASE_NAME);
	}
}
