package domain.id3;

public class InformationGain {

    public InformationGain() {
        // Gain(A) = how much would be gained by branching on A -> we want to pick the attribute that has the highest value for this
        // Gain(A) = Info(D) - Info_A(D)
        // D = tuples at current node N
        // m = number of classes
        // C_i = class i, 1 <= i <= m
        // Info(D) = expected information required to classify a tuple in D
        // Info(D) = -sum_i=1-m( (|C_i,D| / |D|) * log_2(|C_i,D| / |D|) ) = entropy of D
        // Info_A(D) = expected information required to classify a tuple from D based on partitioning by A
        // Info_A(D) = sum_j=1-v( (|D_j| / |D|) * Info(D_j) )
    }

}
