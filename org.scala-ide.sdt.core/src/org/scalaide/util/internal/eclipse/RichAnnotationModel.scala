package org.scalaide.util.internal.eclipse

import org.eclipse.jface.text.source.AnnotationModel
import org.eclipse.jface.text.source.Annotation
import org.eclipse.jface.text.source.IAnnotationModel
import org.eclipse.jface.text.source.IAnnotationModelExtension
import org.eclipse.jface.text.Position
import scala.collection.JavaConversions._
import org.eclipse.jface.text.ISynchronizable

object RichAnnotationModel {

  implicit class RichModel(annotationModel: IAnnotationModel) {

    def withLock[T](f: => T): T = annotationModel match {
      case synchronizable: ISynchronizable =>
        synchronizable.getLockObject.synchronized { f }
      case _ =>
        f
    }

    def getAnnotations: List[Annotation] = {
      val annotations = annotationModel.getAnnotationIterator collect { case ann: Annotation => ann }
      annotations.toList
    }

    def replaceAnnotations(annotations: Iterable[Annotation], replacements: Map[Annotation, Position]) {
      annotationModel.asInstanceOf[IAnnotationModelExtension].replaceAnnotations(annotations.toArray, replacements)
    }

    def deleteAnnotations(annotations: Iterable[Annotation]) {
      replaceAnnotations(annotations, Map())
    }

  }

}
