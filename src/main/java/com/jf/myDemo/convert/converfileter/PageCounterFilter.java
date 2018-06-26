/*
 * Copyright 2004 - 2012 Mirko Nasato and contributors
 *           2016 - 2017 Simon Braconnier and contributors
 *
 * This file is part of JODConverter - Java OpenDocument Converter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jf.myDemo.convert.converfileter;

import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.UnoRuntime;
import org.jodconverter.filter.Filter;
import org.jodconverter.filter.FilterChain;
import org.jodconverter.office.OfficeContext;
import org.jodconverter.office.OfficeException;

/** This filter is used to count the number of pages of a document. */
public class PageCounterFilter implements Filter, FilterChain {

  private int pageCount;

  @Override
  public void doFilter(
          final OfficeContext context, final XComponent document, final FilterChain chain)
      throws Exception {

    // Save the PageCount property of the document.
    final XPropertySet propertySet =
        UnoRuntime.queryInterface(
            XPropertySet.class,
            UnoRuntime.queryInterface(XModel.class, document).getCurrentController());
    pageCount =  (int)propertySet.getPropertyValue("PageCount");

    // Invoke the next filter in the chain
    chain.doFilter(context, document);
  }

  /**
   * Gets the number of pages within the document when the filter has been invoked.
   *
   * @return The number of pages.
   */
  public int getPageCount() {

    return this.pageCount;
  }

  @Override
  public void addFilter(Filter filter) {

  }

  @Override
  public void doFilter(OfficeContext context, XComponent document) throws OfficeException {

  }
}
