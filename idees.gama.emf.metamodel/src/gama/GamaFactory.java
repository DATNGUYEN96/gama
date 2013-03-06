/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gama;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gama.GamaPackage
 * @generated
 */
public interface GamaFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GamaFactory eINSTANCE = gama.impl.GamaFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>EGama Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EGama Model</em>'.
	 * @generated
	 */
	EGamaModel createEGamaModel();

	/**
	 * Returns a new object of class '<em>EGama Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EGama Object</em>'.
	 * @generated
	 */
	EGamaObject createEGamaObject();

	/**
	 * Returns a new object of class '<em>ESpecies</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ESpecies</em>'.
	 * @generated
	 */
	ESpecies createESpecies();

	/**
	 * Returns a new object of class '<em>EAction</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EAction</em>'.
	 * @generated
	 */
	EAction createEAction();

	/**
	 * Returns a new object of class '<em>EAspect</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EAspect</em>'.
	 * @generated
	 */
	EAspect createEAspect();

	/**
	 * Returns a new object of class '<em>EReflex</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EReflex</em>'.
	 * @generated
	 */
	EReflex createEReflex();

	/**
	 * Returns a new object of class '<em>EExperiment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EExperiment</em>'.
	 * @generated
	 */
	EExperiment createEExperiment();

	/**
	 * Returns a new object of class '<em>EGUI Experiment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EGUI Experiment</em>'.
	 * @generated
	 */
	EGUIExperiment createEGUIExperiment();

	/**
	 * Returns a new object of class '<em>EBatch Experiment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EBatch Experiment</em>'.
	 * @generated
	 */
	EBatchExperiment createEBatchExperiment();

	/**
	 * Returns a new object of class '<em>EGama Link</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EGama Link</em>'.
	 * @generated
	 */
	EGamaLink createEGamaLink();

	/**
	 * Returns a new object of class '<em>ESub Species Link</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ESub Species Link</em>'.
	 * @generated
	 */
	ESubSpeciesLink createESubSpeciesLink();

	/**
	 * Returns a new object of class '<em>EAction Link</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EAction Link</em>'.
	 * @generated
	 */
	EActionLink createEActionLink();

	/**
	 * Returns a new object of class '<em>EAspect Link</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EAspect Link</em>'.
	 * @generated
	 */
	EAspectLink createEAspectLink();

	/**
	 * Returns a new object of class '<em>EReflex Link</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EReflex Link</em>'.
	 * @generated
	 */
	EReflexLink createEReflexLink();

	/**
	 * Returns a new object of class '<em>EDisplay Link</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EDisplay Link</em>'.
	 * @generated
	 */
	EDisplayLink createEDisplayLink();

	/**
	 * Returns a new object of class '<em>EDisplay</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EDisplay</em>'.
	 * @generated
	 */
	EDisplay createEDisplay();

	/**
	 * Returns a new object of class '<em>EGrid</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EGrid</em>'.
	 * @generated
	 */
	EGrid createEGrid();

	/**
	 * Returns a new object of class '<em>EVariable</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EVariable</em>'.
	 * @generated
	 */
	EVariable createEVariable();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	GamaPackage getGamaPackage();

} //GamaFactory
