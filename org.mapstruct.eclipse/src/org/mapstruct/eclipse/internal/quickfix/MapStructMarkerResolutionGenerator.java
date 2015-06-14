/**
 *  Copyright 2012-2015 Gunnar Morling (http://www.gunnarmorling.de/)
 *  and/or other contributors as indicated by the @authors tag. See the
 *  copyright.txt file in the distribution for a full listing of all
 *  contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mapstruct.eclipse.internal.quickfix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;
import org.mapstruct.eclipse.internal.quickfix.fixes.AddIgnoreTargetMappingAnnotationQuickFix;

/**
 * Creates possible marker resolutions, i.e. quick fixes for errors that read like being generated by MapStruct
 *
 * @author Andreas Gudian
 */
public class MapStructMarkerResolutionGenerator implements IMarkerResolutionGenerator {

    private static final IMarkerResolution[] NO_RESOLUTIONS = new IMarkerResolution[0];

    private Collection<? extends MapStructQuickFix> allFixes = allQuickFixes();

    @Override
    public IMarkerResolution[] getResolutions(IMarker mk) {
        try {
            if ( isAptCompileProblem( mk ) ) {
                return findApplicableFixes( mk );
            }
        }
        catch ( CoreException e ) {
            return NO_RESOLUTIONS;
        }
        return NO_RESOLUTIONS;
    }

    private static boolean isAptCompileProblem(IMarker mk) throws CoreException {
        return "org.eclipse.jdt.apt.pluggable.core.compileProblem".equals( mk.getType() );
    }

    private IMarkerResolution[] findApplicableFixes(IMarker mk) throws CoreException {
        List<IMarkerResolution> result = new ArrayList<IMarkerResolution>();
        for ( MapStructQuickFix fix : allFixes ) {
            if ( fix.canFix( mk ) ) {
                result.add( fix );
            }
        }
        return result.toArray( new IMarkerResolution[result.size()] );
    }

    private static Collection<? extends MapStructQuickFix> allQuickFixes() {
        return Arrays.asList(
                     new AddIgnoreTargetMappingAnnotationQuickFix()
                     );
    }
}