/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2023 Elior "Mallowigi" Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.mallowigi.icons.svgpatchers

import com.intellij.util.io.DigestUtil
import com.mallowigi.config.AtomSettingsConfig
import org.w3c.dom.Element
import java.nio.charset.StandardCharsets
import javax.swing.UIManager

/** Big icons patcher. */
class BigIconsPatcher : SvgPatcher {

  private var customIconSize = REGULAR
  private var customLineHeight = REGULAR
  private var defaultRowHeight = UIManager.getInt(ROW_HEIGHT)
  private var hasCustomLineHeight = false
  private var hasCustomSize = false

  override fun digest(): ByteArray? {
    val hasher = DigestUtil.sha512()
    hasher.update(hasCustomSize.toString().toByteArray(StandardCharsets.UTF_8))
    hasher.update(hasCustomLineHeight.toString().toByteArray(StandardCharsets.UTF_8))
    hasher.update(customIconSize.toString().toByteArray(StandardCharsets.UTF_8))
    hasher.update(customLineHeight.toString().toByteArray(StandardCharsets.UTF_8))
    return hasher.digest()
  }

  override fun patch(svg: Element, path: String?): Unit = patchSizes(svg)

  override fun priority(): Int = 97

  override fun refresh(): Unit = refreshSizes()

  private fun patchSizes(svg: Element) {
    val isBig = svg.getAttribute(SvgPatcher.BIG)
    val customFontSize = AtomSettingsConfig.instance.customIconSize.toString()
    val hasCustomSize = AtomSettingsConfig.instance.hasCustomIconSize
    val size = if (hasCustomSize) customFontSize else REGULAR

    if (isBig == SvgPatcher.TRUE) {
      svg.setAttribute(SvgPatcher.WIDTH, size.toString())
      svg.setAttribute(SvgPatcher.HEIGHT, size.toString())
    }
  }

  private fun refreshSizes() {
    hasCustomSize = AtomSettingsConfig.instance.hasCustomIconSize
    hasCustomLineHeight = AtomSettingsConfig.instance.hasCustomLineHeight
    customIconSize = AtomSettingsConfig.instance.customIconSize
    customLineHeight = AtomSettingsConfig.instance.customLineHeight

    updateRowHeight()
  }

  private fun updateRowHeight() {
    val extraHeight = if (hasCustomSize) defaultRowHeight + customIconSize - MIN_SIZE else defaultRowHeight
    val customRowHeight = if (hasCustomLineHeight) customLineHeight else extraHeight
    val materialHeight = UIManager.getInt(MATERIAL_ROW_HEIGHT)

    if (materialHeight != 0) {
      UIManager.put(ROW_HEIGHT, materialHeight)
    } else {
      UIManager.put(ROW_HEIGHT, customRowHeight)
    }
  }

  companion object {
    private const val MIN_SIZE = 12
    private const val REGULAR = 16
    private const val ROW_HEIGHT = "Tree.rowHeight"
    private const val MATERIAL_ROW_HEIGHT = "Tree.materialRowHeight"
  }

}
