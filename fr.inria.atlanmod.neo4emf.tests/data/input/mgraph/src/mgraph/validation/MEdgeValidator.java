/**
 *
 * $Id$
 */
package mgraph.validation;

import mgraph.MGraph;
import mgraph.MNode;

/**
 * A sample validator interface for {@link mgraph.MEdge}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface MEdgeValidator {
	boolean validate();

	boolean validateName(String value);
	boolean validateInComing(MNode value);
	boolean validateOutGoing(MNode value);
	boolean validateGraph(MGraph value);
}
