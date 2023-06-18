package com.github.inxilpro.windyidea.listeners

import com.github.inxilpro.windyidea.ClassNameSorter
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute

internal class WindyIdeaDocumentListener : FileDocumentManagerListener {
    private val classNameAttributes = setOf("class", "className")

    override fun beforeAllDocumentsSaving() {
        val unsavedDocuments = FileDocumentManager.getInstance().unsavedDocuments;

        for (document in unsavedDocuments) {
            val psiFile = getPsiFileForDocument(document) ?: continue;

            WriteCommandAction.runWriteCommandAction(psiFile.project) {
                PsiTreeUtil.findChildrenOfType(psiFile, XmlAttribute::class.java)
                    .filter { classNameAttributes.contains(it.name) }
                    .forEach { formatHtmlClass(it) }
            }
        }
    }

    private fun formatHtmlClass(attribute: XmlAttribute) {
        val classNames = attribute.valueElement ?: return
        attribute.setValue(ClassNameSorter.sortClasses(classNames.value))
    }

    private fun getPsiFileForDocument(document: Document): PsiFile? {
        val allEditors = EditorFactory.getInstance().allEditors
        val editor = allEditors.firstOrNull { it.document == document } ?: return null
        val project = editor.project ?: return null

        return PsiDocumentManager.getInstance(project)
            .getPsiFile(document)
            ?.viewProvider
            ?.getPsi(HTMLLanguage.INSTANCE)
    }
}
