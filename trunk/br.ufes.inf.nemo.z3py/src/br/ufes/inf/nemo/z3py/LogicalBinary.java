/**
 */
package br.ufes.inf.nemo.z3py;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Logical Binary</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link br.ufes.inf.nemo.z3py.LogicalBinary#getOperand1 <em>Operand1</em>}</li>
 *   <li>{@link br.ufes.inf.nemo.z3py.LogicalBinary#getOperand2 <em>Operand2</em>}</li>
 * </ul>
 * </p>
 *
 * @see br.ufes.inf.nemo.z3py.Z3pyPackage#getLogicalBinary()
 * @model abstract="true"
 * @generated
 */
public interface LogicalBinary extends Expression {
	/**
	 * Returns the value of the '<em><b>Operand1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operand1</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operand1</em>' containment reference.
	 * @see #setOperand1(Expression)
	 * @see br.ufes.inf.nemo.z3py.Z3pyPackage#getLogicalBinary_Operand1()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Expression getOperand1();

	/**
	 * Sets the value of the '{@link br.ufes.inf.nemo.z3py.LogicalBinary#getOperand1 <em>Operand1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operand1</em>' containment reference.
	 * @see #getOperand1()
	 * @generated
	 */
	void setOperand1(Expression value);

	/**
	 * Returns the value of the '<em><b>Operand2</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operand2</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operand2</em>' containment reference.
	 * @see #setOperand2(Expression)
	 * @see br.ufes.inf.nemo.z3py.Z3pyPackage#getLogicalBinary_Operand2()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Expression getOperand2();

	/**
	 * Sets the value of the '{@link br.ufes.inf.nemo.z3py.LogicalBinary#getOperand2 <em>Operand2</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operand2</em>' containment reference.
	 * @see #getOperand2()
	 * @generated
	 */
	void setOperand2(Expression value);

} // LogicalBinary