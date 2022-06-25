/*
 * The MIT License (MIT)
 *
 *  Copyright (c) 2015-2022 Elior "Mallowigi" Boukhobza
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mallowigi.icons.svgpatchers

import com.intellij.util.io.DigestUtil
import com.mallowigi.config.select.AtomSelectConfig
import org.w3c.dom.Element

/** Custom Color Patcher. */
class CustomColorPatcher : SvgPatcher {
  private var touched = false
  
  override fun refresh(): Unit {
    touched = true
  }

  override fun patch(svg: Element, path: String?) {
    patchIconColor(svg)
    patchFolderColor(svg)
  }

  override fun priority(): Int = 101

  override fun digest(): ByteArray? {
    val hasher = DigestUtil.sha512()
//    hasher.update(touched.toString().toByteArray(StandardCharsets.UTF_8))
    return hasher.digest()
  }

  private fun patchIconColor(svg: Element) {
    val attr = svg.getAttribute(SvgPatcher.ICONCOLOR) ?: return
    val matchingAssociation = AtomSelectConfig.instance.findAssociationByName(attr) ?: return
    val iconColor = matchingAssociation.iconColor

    svg.setAttribute(SvgPatcher.FILL, "#$iconColor")
    svg.setAttribute(SvgPatcher.STROKE, "#$iconColor")
  }

  private fun patchFolderColor(svg: Element) {
    val attr = svg.getAttribute(SvgPatcher.FOLDERCOLOR) ?: return
    val matchingAssociation = AtomSelectConfig.instance.findAssociationByName(attr) ?: return
    val folderColor = matchingAssociation.folderColor

    svg.setAttribute(SvgPatcher.FILL, "#$folderColor")
    svg.setAttribute(SvgPatcher.STROKE, "#$folderColor")
  }
}
