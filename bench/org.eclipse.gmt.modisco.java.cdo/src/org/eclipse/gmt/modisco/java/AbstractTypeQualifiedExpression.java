/**
 * Copyright (c) 2009 Mia-Software.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Fabien Giquel (Mia-Software) - initial API and implementation
 *     Gregoire DUPE (Mia-Software) - initial API and implementation
 */
package org.eclipse.gmt.modisco.java;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Type Qualified Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.gmt.modisco.java.AbstractTypeQualifiedExpression#getQualifier <em>Qualifier</em>}</li>
 * </ul>
 *
 * @see org.eclipse.gmt.modisco.java.cdo.meta.JavaPackage#getAbstractTypeQualifiedExpression()
 * @model abstract="true"
 * @generated
 */
public interface AbstractTypeQualifiedExpression extends Expression {
    /**
     * Returns the value of the '<em><b>Qualifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Qualifier</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Qualifier</em>' containment reference.
     * @see #setQualifier(TypeAccess)
     * @see org.eclipse.gmt.modisco.java.cdo.meta.JavaPackage#getAbstractTypeQualifiedExpression_Qualifier()
     * @model containment="true" ordered="false"
     * @generated
     */
    TypeAccess getQualifier();

    /**
     * Sets the value of the '{@link org.eclipse.gmt.modisco.java.AbstractTypeQualifiedExpression#getQualifier <em>Qualifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Qualifier</em>' containment reference.
     * @see #getQualifier()
     * @generated
     */
    void setQualifier(TypeAccess value);

} // AbstractTypeQualifiedExpression
