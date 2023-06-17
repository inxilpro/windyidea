package com.github.inxilpro.windyidea.listeners

import com.github.inxilpro.windyidea.ClassNameSorter
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag

internal class WindyIdeaDocumentListener : FileDocumentManagerListener {
    override fun beforeAllDocumentsSaving() {
        val unsavedDocuments = FileDocumentManager.getInstance().unsavedDocuments;

        for (document in unsavedDocuments) {
            val psiFile = getPsiFileForDocument(document) ?: continue;

            WriteCommandAction.runWriteCommandAction(psiFile.project) {
                PsiTreeUtil.findChildrenOfType(psiFile, XmlTag::class.java).forEach { tag ->
                    tag.attributes
                        .filter { it.name == "class" || it.name == "className" }
                        .forEach { formatHtmlClass(it) }
                }
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

        return PsiDocumentManager.getInstance(project).getPsiFile(document)
    }
}
