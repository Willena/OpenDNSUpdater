package fr.guillaumevillena.opendnsupdater.AsyncTasks;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import fr.guillaumevillena.opendnsupdater.R;
import fr.guillaumevillena.opendnsupdater.Utils.DnsServersDetector;

import static fr.guillaumevillena.opendnsupdater.TestState.ERROR;
import static fr.guillaumevillena.opendnsupdater.TestState.SUCCESS;

/**
 * Created by guill on 29/06/2018.
 */

public class DnsUsageCheckerTask extends BasicAsyncTask<Context, Void> {
    private static final String TAG = DnsUsageCheckerTask.class.getSimpleName();

    @Override
    protected ResultItem doInBackground(Context... contexts) {

        ResultItem resultItem = new ResultItem();
        resultItem.putSucessState(false);

        Context context = contexts[0];

        DnsServersDetector dnsServersDetector = new DnsServersDetector(context);

        List list = Arrays.asList(dnsServersDetector.getServers());
        String dns1 = context.getResources().getString(R.string.dns1_opendns_ip);
        String dns2 = context.getResources().getString(R.string.dns2_opendns_ip);

        Log.d(TAG, "doInBackground: DNS servers are : " + list);

        if (list.contains(dns1) || list.contains(dns2)) {
            resultItem.putSucessState(true);
        }

        Log.d(TAG, "doInBackground: It is almost finished ... ");

        return resultItem;
    }
}
