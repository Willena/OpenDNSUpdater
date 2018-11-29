package fr.guillaumevillena.opendnsupdater.VpnService.service;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;
import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;
import fr.guillaumevillena.opendnsupdater.R;


@RequiresApi(api = Build.VERSION_CODES.N)
public class OpenDnsUpdaterTileService extends TileService {

    @Override
    public void onClick() {
        boolean service = OpenDnsUpdater.switchService();
        Tile tile = getQsTile();
        tile.setLabel(getString(!service ? R.string.activate_opendns : R.string.deactivate_opendns));
        tile.setContentDescription(getString(R.string.app_name));
        tile.setState(service ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        updateTile();
    }

    private void updateTile() {
        boolean activate = OpenDnsVpnService.isActivated();
        Tile tile = getQsTile();
        tile.setLabel(getString(!activate ? R.string.activate_opendns : R.string.deactivate_opendns));
        tile.setContentDescription(getString(R.string.app_name));
        tile.setState(activate ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        tile.updateTile();
    }
}
