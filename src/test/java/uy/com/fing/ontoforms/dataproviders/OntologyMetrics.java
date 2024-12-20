package uy.com.fing.ontoforms.dataproviders;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OntologyMetrics {

    private int classCount;
    private int objectPropertyCount;
    private int dataPropertyCount;
    private int individualCount;

}
