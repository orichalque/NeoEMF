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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Array Creation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.gmt.modisco.java.ArrayCreation#getDimensions <em>Dimensions</em>}</li>
 *   <li>{@link org.eclipse.gmt.modisco.java.ArrayCreation#getInitializer <em>Initializer</em>}</li>
 *   <li>{@link org.eclipse.gmt.modisco.java.ArrayCreation#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.gmt.modisco.java.cdo.meta.JavaPackage#getArrayCreation()
 * @model
 * @generated
 */
public interface ArrayCreation extends Expression {
    /**
     * Returns the value of the '<em><b>Dimensions</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.gmt.modisco.java.Expression}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dimensions</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dimensions</em>' containment reference list.
     * @see org.eclipse.gmt.modisco.java.cdo.meta.JavaPackage#getArrayCreation_Dimensions()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getDimensions();

    /**
     * Returns the value of the '<em><b>Initializer</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Initializer</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Initializer</em>' containment reference.
     * @see #setInitializer(ArrayInitializer)
     * @see org.eclipse.gmt.modisco.java.cdo.meta.JavaPackage#getArrayCreation_Initializer()
     * @model containment="true" ordered="false"
     * @generated
     */
    ArrayInitializer getInitializer();

    /**
     * Sets the value of the '{@link org.eclipse.gmt.modisco.java.ArrayCreation#getInitializer <em>Initializer</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Initializer</em>' containment reference.
     * @see #getInitializer()
     * @generated
     */
    void setInitializer(ArrayInitializer value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' containment reference.
     * @see #setType(TypeAccess)
     * @see org.eclipse.gmt.modisco.java.cdo.meta.JavaPackage#getArrayCreation_Type()
     * @model containment="true" required="true" ordered="false"
     * @generated
     */
    TypeAccess getType();

    /**
     * Sets the value of the '{@link org.eclipse.gmt.modisco.java.ArrayCreation#getType <em>Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' containment reference.
     * @see #getType()
     * @generated
     */
    void setType(TypeAccess value);

} // ArrayCreation
